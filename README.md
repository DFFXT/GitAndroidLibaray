# GitAndroidLibaray

    allprojects {
		  repositories {
			  ...
			  maven { url 'https://jitpack.io' }
		  }
	  }

	  dependencies {
	        implementation 'com.github.DFFXT:GitAndroidLibaray:0.2'
	  }
    
#使用方法
    
    
    val layout = findViewById<TopFixedRecyclerViewWrapper2>(R.id.topFixedRecyclerViewWrapper2)
        val adapter = object :RecyclerView.Adapter<RecyclerView.ViewHolder>(),IAdapter{
            //......
            override fun isFixedItem(position: Int): Boolean {
                /*判断是否是需要固定的item*/
            }

            override fun bindFixedView(itemView: View, position: Int) {
                //通过position处的item给itemView绑定数据
            }
        }
        layout.recyclerView.adapter = adapter
        layout.buildTop(-1,false)
