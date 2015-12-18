package com.andieguo.poi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 宽表查询
 * 
 * @author andieguo
 * 
 */
public class QueryWTableUtil {

	private Configuration conf;

	public QueryWTableUtil() throws Exception {
		conf = ZHBaseConfiguration.getConfiguration();
	}
	
	public List<PoiBean> findByCityAndtypeA(String tableName,String typeA,String city){
		return null;
	}
	
	public List<PoiBean> findByCityAndtypeB(String tableName,String typeA,String typeB,String city){
		return null;
	}
	
	public List<PoiBean> findByCityAndtypeC(String tableName,String typeA,String typeB,String typeC,String city){
		return null;
	}


	/**
	 * 测试成功：根据小类型获取所有的POI
	 * @param tableName
	 * @param typeA
	 * @return
	 */
	public List<PoiBean> findBytypeC(String tableName,String typeA,String typeB,String typeC){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeA,typeB,typeC);
			byte[] endkey = BytesUtil.endkeyGen(typeA,typeB,typeC);
			poiBeans = putPoiBean(tableName, startkey, endkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}
	
	/**
	 * 测试成功：根据中类型获取所有的POI
	 * @param tableName
	 * @param typeA
	 * @return
	 */
	public List<PoiBean> findBytypeB(String tableName,String typeA,String typeB){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeA,typeB);
			byte[] endkey = BytesUtil.endkeyGen(typeA,typeB);
			poiBeans = putPoiBean(tableName, startkey, endkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}
	
	/**
	 * 测试成功：根据大类型获取所有的POI
	 * @param tableName
	 * @param typeA
	 * @return
	 */
	public List<PoiBean> findBytypeA(String tableName,String typeA){
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		try {
			byte[] startkey = BytesUtil.startkeyGen(typeA);
			byte[] endkey = BytesUtil.endkeyGen(typeA);
			poiBeans = putPoiBean(tableName, startkey, endkey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return poiBeans;
	}

	/**
	 * 根据startkey和endkey查询数据并填充到PoiBean集合
	 * @param tableName
	 * @param startkey
	 * @param endkey
	 * @return
	 * @throws IOException
	 */
	private List<PoiBean> putPoiBean(String tableName, byte[] startkey, byte[] endkey) throws IOException {
		List<PoiBean> poiBeans = new ArrayList<PoiBean>();
		HTable table = new HTable(conf, tableName);
		Scan scan = new Scan(startkey, endkey);
		ResultScanner rs = table.getScanner(scan);
		for (Result row : rs) {
			PoiBean poiBean = new PoiBean();
			for (Map.Entry<byte[], byte[]> entry : row.getFamilyMap("info".getBytes()).entrySet()) {
				String column = new String(entry.getKey());
				if(column.equals("name")) poiBean.setName(Bytes.toString(entry.getValue()));
				if(column.equals("address")) poiBean.setAddress(Bytes.toString(entry.getValue()));
				if(column.equals("telephone")) poiBean.setTelephone(Bytes.toString(entry.getValue()));
				if(column.equals("lat")) poiBean.setLat(Bytes.toDouble(entry.getValue()));
				if(column.equals("lng")) poiBean.setLng(Bytes.toDouble(entry.getValue()));
			}
			poiBeans.add(poiBean);
		}
		if (rs != null) rs.close();
		if (table != null) table.close();
		return poiBeans;
	}

}