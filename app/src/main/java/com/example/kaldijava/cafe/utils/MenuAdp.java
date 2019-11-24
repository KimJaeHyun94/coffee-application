package com.example.kaldijava.cafe.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.kaldijava.R;
import com.example.kaldijava.cafe.http.Http_API;
import com.example.kaldijava.cafe.http.Menu;
import com.example.kaldijava.cafe.http.RES;

import butterknife.BindView;
import butterknife.ButterKnife;

import marty_library.ration.com.library.utils.BaseAdapter;
import marty_library.ration.com.library.utils.BaseCallback;
import marty_library.ration.com.library.utils.BaseVH;

public class MenuAdp extends BaseAdapter<Menu, MenuVH> {

    BaseCallback<RES<Object>> callback;
    public MenuAdp(Context mCon,BaseCallback<RES<Object>> callback) {
        super(mCon, R.layout.item_cafe);
        this.callback = callback;
    }

    @Override
    public MenuVH getViewHolder(View view) {
        return new MenuVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuVH menuVH, final int i) {
        super.onBindViewHolder(menuVH, i);
        menuVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Http_API.Apis apis = new Http_API().getApis();
                apis.sendPush(arrayList.get(i).getCoffee()).enqueue(callback);
            }
        });
    }
}

class MenuVH extends BaseVH<Menu> {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.addr)
    TextView addr;
    public MenuVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(final Menu menu, Context context) {
        name.setText(menu.getCoffee());
        try {
            price.setText(String.format("%,d", Integer.parseInt(menu.getPrice())) + " 원");
        }catch (Exception e){
            price.setText(menu.getPrice() + " 원");
        }
        addr.setText(menu.getCafe_name() + "  " + menu.getAddress());


    }
}
