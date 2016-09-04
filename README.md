# LiteRead

### 闲暇时间学习retrofit+rxAndroid的项目.

### 图片数据来源于[天狗开放阅图](http://apistore.baidu.com/apiworks/servicedetail/992.html)

### Android干货分享来源于[Gank.io](http://gank.io/api)

### 直播数据来源于[斗鱼](www.douyu.com),目前因为直播地址的加密,只解析到了直播列表

## 截图

![](/img/Screenshot_2016-09-01-23-05-48.jpeg)
![](/img/Screenshot_2016-09-01-23-06-07.jpeg)
![](/img/Screenshot_2016-09-01-23-06-32.jpeg)

![](/img/Screenshot_2016-09-01-23-06-51.jpeg)
![](/img/Screenshot_2016-09-01-23-07-12.jpeg)
![](/img/Screenshot_2016-09-01-23-07-30.jpeg)


### okhttp拦截器(包括日志打印以及HTTP缓存)

<pre><code>
class LoggerInterceptor implements Interceptor {
        public static final String TAG = "OkHttp";
        private String tag;
        private boolean showResponse;

        public LoggerInterceptor(String tag, boolean showResponse) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            this.showResponse = showResponse;
            this.tag = tag;
        }

        public LoggerInterceptor(String tag) {
            this(tag, true);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            logForRequest(request);
            if (!Network.isConnected(LiteReadApplication.mContext)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Log.e("NoNetwork", "无网络");
            } else
                Log.e("NoNetwork", "有网络");
            Response response = chain.proceed(request);
            if (Network.isConnected(LiteReadApplication.mContext)) {
                int maxAge = 60 * 60; // read from cache for 1 minute
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return logForResponse(response);
        }

        private Response logForResponse(Response response) {
            try {
                //===>response log
                Log.e(tag, "========response'log=======");
                Response.Builder builder = response.newBuilder();
                Response clone = builder.build();
                Log.e(tag, "url : " + clone.request().url());
                Log.e(tag, "code : " + clone.code());
                Log.e(tag, "protocol : " + clone.protocol());
                if (!TextUtils.isEmpty(clone.message()))
                    Log.e(tag, "message : " + clone.message());

                if (showResponse) {
                    ResponseBody body = clone.body();
                    if (body != null) {
                        MediaType mediaType = body.contentType();
                        if (mediaType != null) {
                            Log.e(tag, "responseBody's contentType : " + mediaType.toString());
                            if (isText(mediaType)) {
                                String resp = body.string();
                                Log.e(tag, "responseBody's content : " + resp);

                                body = ResponseBody.create(mediaType, resp);
                                return response.newBuilder().body(body).build();
                            } else {
                                Log.e(tag, "responseBody's content : " + body.string());
                            }
                        }
                    }
                }

                Log.e(tag, "========response'log=======end");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        private void logForRequest(Request request) {
            try {
                String url = request.url().toString();
                Headers headers = request.headers();
                Log.e(tag, "========request'log=======");
                Log.e(tag, "method : " + request.method());
                Log.e(tag, "url : " + url);
                Log.e(tag, "pamars:" + request.toString());
                if (headers != null && headers.size() > 0) {
                    Log.e(tag, "headers : " + headers.toString());
                }
                RequestBody requestBody = request.body();
                if (requestBody != null) {
                    MediaType mediaType = requestBody.contentType();
                    if (mediaType != null) {
                        Log.e(tag, "requestBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            Log.e(tag, "requestBody's content : " + bodyToString(request));
                        } else {
                            Log.e(tag, "requestBody's content : " + bodyToString(request));
                        }
                    }
                }
                Log.e(tag, "========request'log=======end");
            } catch (Exception e) {
//            e.printStackTrace();
            }
        }

        private boolean isText(MediaType mediaType) {
            if (mediaType.type() != null && mediaType.type().equals("text")) {
                return true;
            }
            if (mediaType.subtype() != null) {
                if (mediaType.subtype().equals("json") ||
                        mediaType.subtype().equals("xml") ||
                        mediaType.subtype().equals("html") ||
                        mediaType.subtype().equals("webviewhtml")
                        )
                    return true;
            }
            return false;
        }

        private String bodyToString(final Request request) {
            try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "something error when show requestBody.";
            }
        }
    }
</code></pre>

### RecyclerView的上拉加载更多

#### 通过viewtype判断当前item是否为loadmore项

<pre><code>
 @Override
    public int getItemViewType(int position) {
        if ( getItemCount() > 1&&position == getItemCount() - 1) {
            needLoadMore = true;
            return LOAD_MORE;
        } else {
            needLoadMore = false;
            return NO_LOAD_MORE;
        }
    }
</code></pre>

#### 因为在数据列表中额外加入了loadmore项,因此列表总长度为list.size()+1.

#### 在bindViewHolder中判断是否应该加载loadmore项
<pre><code>
 if (viewType != LOAD_MORE) {
            itemView = LayoutInflater.from(context).inflate(R.layout.image_list_item, parent, false);
            ...
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.load_more_view, parent, false);
            ...
        }
</code></pre>

#### 封装Subscriber统一处理错误log以及进度条展示

<pre><code>
 public class HttpSubscriber<T> extends Subscriber<T> {
    private View view;

     public HttpSubscriber() {
     }

     public HttpSubscriber(View view) {
         this.view = view;
         setProgressBarISvisible(view, true);
     }

     @Override
     public void onCompleted() {
         setProgressBarISvisible(view, false);
     }


     @Override
     public void onError(Throwable e) {
         setProgressBarISvisible(view, false);
         if (e instanceof SocketTimeoutException) {
             Log.e(e.toString());
         } else if (e instanceof HttpException) {
             HttpException httpException = (HttpException) e;
             Log.e(httpException.code() + "");
             Log.e(httpException.message() + "");
             if (httpException.response() != null && httpException.response().errorBody() != null) {
                 try {
                     Log.e(httpException.response().message());
                     String bodyStr = httpException.response().errorBody().string();
                     Log.e(bodyStr);
                 } catch (IOException e1) {
                     e1.printStackTrace();
                 }
             }
         }
     }

     @Override
     public void onNext(T t) {
     }

     public void setProgressBarISvisible(View view, boolean iSvisible) {
         if (view != null)
             if (iSvisible) {
                 view.setVisibility(View.VISIBLE);
             } else {
                 view.setVisibility(View.GONE);
             }
     }
 }
</code></pre>

#### 提供一个带参的构造方法传入ProgressBar以及在没有ProgressBar的时候使用的无参构造方法

### 特别鸣谢:

[rxAndroid](https://github.com/ReactiveX/RxAndroid)

[retrofit](https://github.com/square/retrofit)

[butterknife](https://github.com/JakeWharton/butterknife)

[universal-image-loader](https://github.com/nostra13/Android-Universal-Image-Loader)

### 另外也非常感谢[Trinea](http://www.trinea.cn)的博客以及他的[CodeKK](http://www.codekk.com)给我提供了大量学习的机会


#### 最后的最后,一些恬不知耻的话:

#### 本人应届狗,英语软件双学位,现居深圳西乡,Can you give me a job?

