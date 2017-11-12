package seg_sentence;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.InitialContext;

public class clean_passage {
	static String str;
	static Map<String, Integer> map = new HashMap<String, Integer>();
	static Map<String, Integer> biggram_map = new HashMap<String, Integer>();
	
	public static void Initial() throws IOException{
		load_clean("data/seg_corpus.txt");
		biggram();
	}
	
	public static String load_clean(String org_text) throws IOException{
		 List<String> list=new ArrayList<>();
		 File file=new File(org_text);
	        try {
	            FileInputStream in=new FileInputStream(file);
	            // size  为字串的长度 ，这里一次性读完
	            int size=in.available();
	            byte[] buffer=new byte[size];
	            in.read(buffer);
	            in.close();
	            str=new String(buffer,"utf-8");//将文件原始数据文件读取到字符串str中
	        } catch (IOException e) {
	           // TODO Auto-generated catch block
	            e.printStackTrace();
	        } 
	         str = str.replaceAll("\\d{8}-\\d{2}-\\d{3}-\\d{3}/m","");
	         str = str.replaceAll("\\n|\\pP|\\pS|[a-z]|[A-Z]", "");
	         str = str.replaceAll(" +"," ");
	       return str;
	}
	public static void biggram(){
		
		String [] arr = str.split("\\s+");
		String temp="";
		for(String ss : arr){
			map.put(ss,0);
			biggram_map.put(temp+" "+ss,0);
			//System.out.println(temp+" "+ss);
			temp = ss;
		   
		}
		temp="";
		int count=0;
		for(String ss : arr){
			map.put(ss,map.get(ss)+1);
			biggram_map.put(temp+" "+ss,biggram_map.get(temp+" "+ss)+1);
			temp = ss;
			count++;
		}
	}

	
	public static float com_probability(String word_a,String word_b){
		float w1,w1w2;
		try{
			w1 = map.get(word_a)+map.size();
		}catch(Exception e){
			w1 = map.size();
		}
		try{
			w1w2 = biggram_map.get(word_a+" "+word_b)+1;
		}catch(Exception e){
			w1w2 = 1;
		}
		//System.out.println(w1+"##"+w1w2);
		float probability = w1w2/w1;
		//int probability =0;
		return probability;
	}
	
	public static void main(String[] args) throws IOException{
		Initial();
		System.out.println(com_probability("研究","生命"));
		System.out.println(com_probability("研究生","命"));
		
	}
}
