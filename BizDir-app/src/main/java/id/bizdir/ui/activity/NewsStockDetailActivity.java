package id.bizdir.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import id.bizdir.R;
import id.bizdir.modelhelper.NewsStockHelper;
import id.bizdir.model.NewsStock;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class NewsStockDetailActivity extends AppCompatActivity {

    private NewsStock newsStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_stock_detail);
        Helpers.setLockOrientation(NewsStockDetailActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getDataFromPreviousPage();
        //setTitle(mTitle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        if (objectIndex != 0) {
            NewsStockHelper helper = new NewsStockHelper();
            newsStock = helper.get(objectIndex);
            if (newsStock != null) {
                getData();
            }
        }
    }

    private void getData() {
        final TextView textCompanyName = (TextView) findViewById(R.id.textCompanyName);
        final EditText editStockCode = (EditText) findViewById(R.id.editStockCode);
        final EditText editStockName = (EditText) findViewById(R.id.editStockName);
        final EditText editMarketBoard = (EditText) findViewById(R.id.editMarketBoard);
        final EditText editLast = (EditText) findViewById(R.id.editLast);
        final EditText editOpen = (EditText) findViewById(R.id.editOpen);
        final EditText editChange = (EditText) findViewById(R.id.editChange);
        final EditText editPercentChange = (EditText) findViewById(R.id.editPercentChange);
        final EditText editValue = (EditText) findViewById(R.id.editValue);
        final EditText editVolume = (EditText) findViewById(R.id.editVolume);
        final EditText editHigh = (EditText) findViewById(R.id.editHigh);
        final EditText editLow = (EditText) findViewById(R.id.editLow);
        final EditText editClose = (EditText) findViewById(R.id.editClose);
        final EditText editSource = (EditText) findViewById(R.id.editSource);

        textCompanyName.setText(newsStock.getCompany());
        editStockCode.setText(newsStock.getStock_code());
        editStockName.setText(newsStock.getStock_name());
        editMarketBoard.setText(newsStock.getMarket_board());
        editLast.setText(newsStock.getLast());
        editOpen.setText(newsStock.getOpen());
        editChange.setText(newsStock.getChange());
        editPercentChange.setText(newsStock.getPercent_change());
        editValue.setText(newsStock.getValue());
        editVolume.setText(newsStock.getVolume());
        editHigh.setText(newsStock.getHigh());
        editLow.setText(newsStock.getLow());
        editClose.setText(newsStock.getClose());
        editSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOpenWebsite(editSource.getText().toString());
            }
        });
        editSource.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setOpenWebsite(editSource.getText().toString());
                }
            }
        });
    }

    private void setOpenWebsite(String website) {
        final String[] websiteList = Helpers.getArrayString(website);
        int websiteCount = websiteList.length;
        if (websiteCount > 0) {
            BottomSheet.Builder sheetBuilder = new BottomSheet.Builder(NewsStockDetailActivity.this,
                    R.style.BottomSheet_StyleDialog)
                    .title(getResources().getString(R.string.bottom_sheet_openwebsite)).
                            listener(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int index) {
                                    Helpers.setOpenUrlOnWeb(websiteList[index - 1],
                                            NewsStockDetailActivity.this);
                                }
                            });
            int countId = 1;
            for (String web : websiteList) {
                sheetBuilder.sheet(countId, Helpers.getBottomIcon(FontAwesome.Icon.faw_globe, true),
                        web);
                countId++;
            }
            sheetBuilder.build().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(NewsStockDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
