package com.wenen.literead.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.model.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.model.MaterialAboutTitleItem;
import com.thefinestartist.finestwebview.FinestWebView;
import com.wenen.literead.R;

/**
 * Created by Administrator on 2016/12/28.
 */

public class AboutActivity extends MaterialAboutActivity {
  Toolbar toolbar;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    this.setTitle("关于");
    super.onCreate(savedInstanceState);
    toolbar = (Toolbar) findViewById(com.danielstone.materialaboutlibrary.R.id.mal_toolbar);
  }

  @Override protected void onResume() {
    super.onResume();
    toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onBackPressed();
      }
    });
  }

  @Override

  protected MaterialAboutList getMaterialAboutList() {

    MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

    // Add items to card

    appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()

        .text(R.string.app_name)

        .icon(R.mipmap.ic_launcher)

        .build());

    appCardBuilder.addItem(new MaterialAboutActionItem.Builder().text("Version")
        .subText(new PackageInfo().versionName)
        .icon(R.mipmap.ic_about_info)
        .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
          @Override public void onClick() {
            Toast.makeText(AboutActivity.this, new PackageInfo().versionName, Toast.LENGTH_SHORT)
                .show();
          }
        })
        .build());
    appCardBuilder.addItem(new MaterialAboutActionItem.Builder().text("Licenses")
        .icon(R.mipmap.ic_about_licenses)
        .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
          @Override public void onClick() {
            Toast.makeText(AboutActivity.this, "Licenses Tapped", Toast.LENGTH_SHORT).show();
          }
        })
        .build());

    MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();

    authorCardBuilder.title("Author");
    authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()

        .text("Wen_en")

        .subText("Android Developer")

        .icon(R.mipmap.ic_about_person)

        .build());

    authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()

        .text("Fork on GitHub")

        .icon(R.mipmap.ic_about_github)

        .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
          @Override public void onClick() {
            new FinestWebView.Builder(getApplicationContext()).statusBarColorRes(
                R.color.colorPrimary)
                .progressBarColorRes(R.color.colorPrimary)
                .toolbarColorRes(R.color.colorPrimary)
                .titleColorRes(R.color.white)
                .menuColorRes(R.color.white)
                .iconDefaultColorRes(R.color.white)
                .show("https://github.com/A-W-C-J/LiteRead");
          }
        })

        .build());
    MaterialAboutCard.Builder supportCardBuilder = new MaterialAboutCard.Builder();
    supportCardBuilder.title("Support Development");
    supportCardBuilder.addItem(new MaterialAboutActionItem.Builder().text("Report Bugs")
        .subText("Report bugs or request new features.")
        .icon(R.mipmap.ic_about_bug)
        .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {

          @Override

          public void onClick() {

            Toast.makeText(AboutActivity.this, "Bug report tapped", Toast.LENGTH_SHORT).show();
          }
        })

        .build());

    return new MaterialAboutList.Builder()

        .addCard(appCardBuilder.build())

        .addCard(authorCardBuilder.build())

        .addCard(supportCardBuilder.build())

        .build();
  }

  @Override

  protected CharSequence getActivityTitle() {

    return getString(R.string.mal_title_about);
  }
}
