package seg_sentence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class seg {
	static Map<String, Integer> map = new HashMap<String, Integer>();  
	static clean_passage a = new clean_passage();
	
	public static List<String> LoadFile(String filename) throws IOException{
		
		List<String> list = new ArrayList<String>();
		File file = new File(filename);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
		BufferedReader br = new BufferedReader(reader);
		String line =null;
		while((line = br.readLine())!= null){	
			String[] string = line.split(" ", 3);
			String key = string[0];
			int word_frequency = Integer.parseInt(string[1]);
			map.put(key,word_frequency);
			list.add(key);
		}
		return list;
	}
	
	public static List<String> Loadyuliao(String filename) throws IOException{		
			List<String> list = new ArrayList<String>();
			File file = new File(filename);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(reader);
			String line =null;
			while((line = br.readLine())!= null){	
				String[] string = line.split(" ", 3);
				String key = string[0];
				int word_frequency = Integer.parseInt(string[1]);
				map.put(key,word_frequency);
				list.add(key);
			}
			return list;
		}
		
	
	public static List<String> order_cut_sentence(String sentence,List<String>list){
		List<String> cut_list = new ArrayList<String>();
		while(sentence.length()!=0){
			int cut_point=0;
			int temp = 0;
			String word="";
			for(int j=0;j<sentence.length()+1;j++){
				String word_pre = sentence.substring(0,j);
				//System.out.println(word_pre+"@@"+list.contains(word_pre));///////////
				if(list.contains(word_pre)){
					temp = 1;
					//System.out.println(word_pre+"##");////////////////
					cut_point = j;
				}
				if(temp == 0 &&j == sentence.length()){
					cut_point = sentence.length();
					//System.out.println(word_pre+"##");////////////////
				}
			}
			word = sentence.substring(0,cut_point);
			cut_list.add(word);
			if(cut_point!=sentence.length()){
			   sentence = 	sentence.substring(cut_point, sentence.length());
			}
			else{
				sentence = "";
			}
		}
		return cut_list;
	}
	
	public static List<String> reverse_cut_sentence(String sentence,List<String>list){
		List<String> cut_list = new ArrayList<String>();
		while(sentence.length()!=0){
			int cut_point=0;
			int temp = 0;
			String word="";
			for(int j=sentence.length();j>=0;j--){
				String word_pre = sentence.substring(j,sentence.length());
				//System.out.println(word_pre+"@@"+list.contains(word_pre));///////////
				if(list.contains(word_pre)){
					temp = 1;
					//System.out.println(word_pre+"##");////////////////
					cut_point = j;
				}
				if(temp == 0 &&j == 0){
					cut_point = 0;
				}
			}
			word = sentence.substring(cut_point,sentence.length());
			cut_list.add(word);
			if(cut_point!=0){
			   sentence = 	sentence.substring(0,cut_point);
			}
			else{
				sentence = "";
			}
		}
		Collections.reverse(cut_list);
		return cut_list;
	}
	
	public static List<String> parse(String filedir,String sentence ) throws IOException{
		//sentence = sentence.replaceAll("\\pP|\\pS", "");
		List<String> list = new ArrayList<String>();
		List<String> order_cut_list = new ArrayList<String>();
		List<String> reverse_cut_list = new ArrayList<String>();
		list = LoadFile(filedir);//加载词典
		
		//System.out.println(sentence);		
		order_cut_list = order_cut_sentence(sentence,list);
		reverse_cut_list = reverse_cut_sentence(sentence,list);
		if(!order_cut_list.containsAll(reverse_cut_list)){
		
		int order_probability = 1;
		int reverse_probability = 1;
		for(int i = 1 ; i < order_cut_list.size() ; i++) {
			order_probability *=a.com_probability(order_cut_list.get(i-1),order_cut_list.get(i));
			}
		for(int i = 1 ; i < reverse_cut_list.size() ; i++) {
			reverse_probability *=a.com_probability(reverse_cut_list.get(i-1),reverse_cut_list.get(i));
			}
		
		if(order_probability > reverse_probability)
			return order_cut_list;
		else 
			return reverse_cut_list;
		}
		return order_cut_list;
	}
	
	public static void main(String[] args) throws IOException{
		List<String> list = new ArrayList<String>();
		String filedir = "data/word_dic.txt";
		System.out.println("正在加载语料库,请稍后。。。");
		a.Initial();
		System.out.println("加载成功\n");
		//"南京市长江大桥"  "很多同学跟我一样" "我今晚得到达南京" "香槟酒非常好喝","有些跟我一样手足无措" "研究生命的起源";
		Scanner in=new Scanner(System.in);
		String sentence=null;  
		while(true){
			System.out.println("请输入要分词的语句:");
			sentence = in.next();
			list = parse(filedir,sentence);
			for(String word:list){
				System.out.print(word+"/");
			}
			System.out.print("\n\n");
		}	
	}
}

















