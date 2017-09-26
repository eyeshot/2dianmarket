package core.util;

import java.util.Random;

public class StringUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//String temp = "data:image/jpeg;base64,/ssss";
		//String temp2 = temp.substring(0, temp.indexOf(","));
		//String temp3 = temp.substring(temp.indexOf(",") + 1);
		//System.out.println("temp2:" + temp2);
		//System.out.println("temp3:" + temp3);
		//String temp = IDGenerator.next();
		//System.out.println(temp);
		//System.out.println(temp.length());
		
		new Thread(new Runnable() {  
            @Override  
            public void run() {  
                for(int i=0;i<5;i++){  
                    try {  
                        Thread.currentThread().sleep(i*1000);  
                        System.out.println("睡了"+i*10+"秒");  
                    } catch (InterruptedException e) {  
                        System.out.println("干嘛吵醒我");  
                    }  
                }  
            }  
        }).start();   
          
        for(int i=0;i<50;i++){  
                System.out.print(i);  
        }  

	}
	
	public static String getRandomStr(int number)
	{
		Random random = new Random();
		int randomInt = random.nextInt(number);
		return String.valueOf(randomInt);
	}

}
