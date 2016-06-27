package com.joke;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joke.adapter.ShopAdapter;
import com.joke.info.Product;

public class ShopMain extends Activity implements View.OnClickListener,
		AdapterView.OnItemClickListener {

	private List<Product> datas; // 数据源
	private ShopAdapter adapter; // 自定义适配器
	private ListView listView; // ListView控件
	private TextView text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopmain);

		 // 模拟数据
        datas = new ArrayList<Product>();
        Product product = null;
        for (int i = 0; i < 30; i++) {
            product = new Product();
            product.setName("商品："+i+":单价:"+i);
            product.setNum(1);
            product.setPrice(i);
            datas.add(product);
        }
        adapter = new ShopAdapter(datas,this);
        listView.setAdapter(adapter);
		// 注册观察者
		//adapter.registerDataSetObserver(sumObserver);
		text = (TextView) findViewById(R.id.text);
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);

		// 以上就是我们常用的自定义适配器ListView展示数据的方法了

		// 解决问题：在哪里处理按钮的点击响应事件，是适配器 还是 Activity或者Fragment，这里是在Activity本身处理接口
		// 执行添加商品数量，减少商品数量的按钮点击事件接口回调
		 adapter.setOnAddNum(this);
		 adapter.setOnSubNum(this);
		// / listView.setOnItemClickListener(this);
//		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View view,
//					int i, long l) {
//				//list.set(i, 2);
//				// 将列表项的0变为2 更新适配器，
//				adapter.notifyDataSetChanged();
//				// 执行该方法后DataSetObserver观察者观察到
//			}
//		});

	}

	//

	// 3、点击某个按钮的时候，如果列表项所需的数据改变了，如何更新UI
	// //解决问题：点击某个按钮的时候，如果列表项所需的数据改变了，如何更新UI
	@Override
	public void onClick(View view) {
		Object tag = view.getTag();
		switch (view.getId()) {
		case R.id.item_btn_add: // 点击添加数量按钮，执行相应的处理
			// 获取 Adapter 中设置的 Tag
			if (tag != null && tag instanceof Integer) { // 解决问题：如何知道你点击的按钮是哪一个列表项中的，通过Tag的position
				int position = (Integer) tag;
				// 更改集合的数据
				int num = datas.get(position).getNum();
				num++;
				datas.get(position).setNum(num); // 修改集合中商品数量
				datas.get(position).setPrice(position * num); // 修改集合中该商品总价
																// 数量*单价
				adapter.notifyDataSetChanged();
			}
			break;
		case R.id.item_btn_sub: // 点击减少数量按钮 ，执行相应的处理
			// 获取 Adapter 中设置的 Tag
			if (tag != null && tag instanceof Integer) {
				int position = (Integer) tag;
				// 更改集合的数据
				int num = datas.get(position).getNum();
				if (num > 0) {
					num--;
					datas.get(position).setNum(num); // 修改集合中商品数量
					datas.get(position).setPrice(position * num); // 修改集合中该商品总价
																	// 数量*单价
					adapter.notifyDataSetChanged();
				}
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		Toast.makeText(ShopMain.this, "点击了第" + i + "个列表项", Toast.LENGTH_SHORT)
				.show();
	}

	private DataSetObserver sumObserver = new DataSetObserver() {
		/**
		 * 当Adapter的notifyDataSetChanged方法执行时被调用 //执行相应的操作
		 */
		@Override
		public void onChanged() {
			super.onChanged();
			// 执行相应的操作
			int sum = 0;
			for (int i = 0; i < datas.size(); i++) {
				sum += datas.get(i).getPrice();
			}
			text.setText("总金额:" + sum);
		}

		/**
		 * 当Adapter 调用 notifyDataSetInvalidate方法执行时被调用 　//执行相应的操作
		 */
		@Override
		public void onInvalidated() {
			super.onInvalidated();
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销观察者
		adapter.unregisterDataSetObserver(sumObserver);
	}

}