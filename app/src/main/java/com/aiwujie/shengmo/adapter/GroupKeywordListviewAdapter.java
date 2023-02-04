package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GroupSearchKeywordActivity;
import com.aiwujie.shengmo.activity.SearchGroupActivity;
import com.aiwujie.shengmo.bean.KeyWordCustomData;
import com.aiwujie.shengmo.bean.KeyWordData;
import com.aiwujie.shengmo.customview.MyGridview;
import com.aiwujie.shengmo.kt.ui.activity.statistical.GroupSearchResultActivity;
import com.aiwujie.shengmo.view.FlowLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class GroupKeywordListviewAdapter extends BaseAdapter {
    private Context context;
    private List<KeyWordData.DataBean> list;
    private LayoutInflater inflater;
    List<KeyWordCustomData> keyWordCustomDatas=new ArrayList<>();
    List<String> keywors00=new ArrayList<>();
    List<String> keywors01=new ArrayList<>();
    List<String> keywors02=new ArrayList<>();
    List<String> keywors03=new ArrayList<>();
    List<String> keywors04=new ArrayList<>();
    List<String> keywors05=new ArrayList<>();
    List<String> keywors06=new ArrayList<>();
    public GroupKeywordListviewAdapter(Context context, List<KeyWordData.DataBean> list) {
        super();
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getPid().equals("0")){
                keywors00.add(list.get(i).getContent());
                KeyWordCustomData data=new KeyWordCustomData();
                data.setPid("0");
                data.setKeywords(keywors00);
                keyWordCustomDatas.add(data);
            }
            if(list.get(i).getPid().equals("1")){
                keywors01.add(list.get(i).getContent());
                KeyWordCustomData data=new KeyWordCustomData();
                data.setPid("1");
                data.setKeywords(keywors01);
                keyWordCustomDatas.add(data);
            }
            if(list.get(i).getPid().equals("2")){
                keywors02.add(list.get(i).getContent());
                KeyWordCustomData data=new KeyWordCustomData();
                data.setPid("2");
                data.setKeywords(keywors02);
                keyWordCustomDatas.add(data);
            }
            if(list.get(i).getPid().equals("3")){
                keywors03.add(list.get(i).getContent());
                KeyWordCustomData data=new KeyWordCustomData();
                data.setPid("3");
                data.setKeywords(keywors03);
                keyWordCustomDatas.add(data);
            }
            if(list.get(i).getPid().equals("4")){
                keywors04.add(list.get(i).getContent());
                KeyWordCustomData data=new KeyWordCustomData();
                data.setPid("4");
                data.setKeywords(keywors04);
                keyWordCustomDatas.add(data);
            }
            if(list.get(i).getPid().equals("5")){
                keywors05.add(list.get(i).getContent());
                KeyWordCustomData data=new KeyWordCustomData();
                data.setPid("5");
                data.setKeywords(keywors05);
                keyWordCustomDatas.add(data);
            }
            if(list.get(i).getPid().equals("6")){
                keywors06.add(list.get(i).getContent());
                KeyWordCustomData data=new KeyWordCustomData();
                data.setPid("6");
                data.setKeywords(keywors06);
                keyWordCustomDatas.add(data);
            }
        }

        Collections.sort(keyWordCustomDatas, new Comparator<KeyWordCustomData>(){
            public int compare(KeyWordCustomData o1, KeyWordCustomData o2) {
                //按照pid排序
                if(Integer.parseInt(o1.getPid()) > Integer.parseInt(o2.getPid())){
                    return 1;
                }
                if(Integer.parseInt(o1.getPid()) == Integer.parseInt(o2.getPid())){
                    return 0;
                }
                return -1;
            }
        });
        //去除重复
        for (int i = 0; i < keyWordCustomDatas.size(); i++) {
            for (int j = keyWordCustomDatas.size() - 1 ; j > i; j--) {
                if (keyWordCustomDatas.get(i).getPid().equals(keyWordCustomDatas.get(j).getPid())) {
                    keyWordCustomDatas.remove(j);
                }
            }
        }
        for (int i = 0; i < keyWordCustomDatas.size(); i++) {
            Log.i("GroupKeywordListview", "GroupKeywordListviewAdapter: "+keyWordCustomDatas.get(i).getPid()+","+keyWordCustomDatas.get(i).getKeywords());
        }
    }

    @Override
    public int getCount() {
        return keyWordCustomDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return keyWordCustomDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_group_keyword,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String keyword;
        if(keyWordCustomDatas.get(position).getPid().equals("1")){
            keyword="推荐关键词";
            holder.itemListviewGroupTvKeyword.setText(keyword);
        }else if(keyWordCustomDatas.get(position).getPid().equals("2")){
            keyword="生活休闲";
            holder.itemListviewGroupTvKeyword.setText(keyword);
        }else if(keyWordCustomDatas.get(position).getPid().equals("3")){
            keyword="省市地区";
            holder.itemListviewGroupTvKeyword.setText(keyword);
        }else if(keyWordCustomDatas.get(position).getPid().equals("4")){
            keyword="同好人群";
            holder.itemListviewGroupTvKeyword.setText(keyword);
        }else if(keyWordCustomDatas.get(position).getPid().equals("5")){
            keyword="圣魔爱好";
            holder.itemListviewGroupTvKeyword.setText(keyword);
        }else if(keyWordCustomDatas.get(position).getPid().equals("6")){
            keyword="其他分类";
            holder.itemListviewGroupTvKeyword.setText(keyword);
        } else if (keyWordCustomDatas.get(position).getPid().equals("0")) {
            keyword = "最热关键词";
            holder.itemListviewGroupTvKeyword.setText(keyword);
        }

        if (keyWordCustomDatas.get(position).getPid().equals("0")) {
            holder.flowLayout.initData(keyWordCustomDatas.get(position).getKeywords());
           // holder.flowLayout.initData(Arrays.asList("1","2","3","4","5","6","7","8","9","10"));
            holder.flowLayout.setVisibility(View.VISIBLE);
            holder.itemListviewGroupKeywordGridview.setVisibility(View.GONE);
            holder.flowLayout.setOnFlowItemClickListener(new FlowLayout.OnFlowItemClickListener() {
                @Override
                public void onFlowItemClick(@NotNull String str) {
                    GroupSearchKeywordActivity.mGroupSearchKeyWordEtSearch.setText(str);
                    GroupSearchKeywordActivity.mGroupSearchKeyWordEtSearch.setSelection(GroupSearchKeywordActivity.mGroupSearchKeyWordEtSearch.getText().length());
                    //Intent intent = new Intent(context, SearchGroupActivity.class);
                    Intent intent = new Intent(context, GroupSearchResultActivity.class);
                    intent.putExtra("search", str);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.flowLayout.setVisibility(View.GONE);
            holder.itemListviewGroupKeywordGridview.setVisibility(View.VISIBLE);
            holder.itemListviewGroupKeywordGridview.setTag(position);
            holder.itemListviewGroupKeywordGridview.setAdapter(new GroupKeywordGridviewAdapter(context,keyWordCustomDatas.get(position).getKeywords()));
            holder.itemListviewGroupKeywordGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos= (int) parent.getTag();
//                EventBus.getDefault().post(new KeyWordEvent(keyWordCustomDatas.get(pos).getKeywords().get(position)));
                    GroupSearchKeywordActivity.mGroupSearchKeyWordEtSearch.setText(keyWordCustomDatas.get(pos).getKeywords().get(position));
                    GroupSearchKeywordActivity.mGroupSearchKeyWordEtSearch.setSelection(GroupSearchKeywordActivity.mGroupSearchKeyWordEtSearch.getText().length());
                    //Intent intent = new Intent(context, SearchGroupActivity.class);
                    Intent intent = new Intent(context, GroupSearchResultActivity.class);
                    intent.putExtra("search", keyWordCustomDatas.get(pos).getKeywords().get(position));
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_listview_group_tv_keyword)
        TextView itemListviewGroupTvKeyword;
        @BindView(R.id.item_listview_group_keyword_gridview)
        MyGridview itemListviewGroupKeywordGridview;
        @BindView(R.id.flow_item_keyword)
        FlowLayout flowLayout;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


