package ImageConvert;

import java.awt.image.BufferedImage;
import java.lang.*;


//Just to check if git is working

public class ImageCompare {
	private BufferedImage test,gt;
	
	public ImageCompare(BufferedImage test,BufferedImage ground_t) {
		this.test = test;
		this.gt = ground_t;
	}
	
	public double compare() {
		long pixnum = (long)test.getHeight()*test.getWidth(),error=0;
		for(int i=0;i<test.getHeight();i++) {
			for(int j=0;j<test.getWidth();j++) {
				int p1 = test.getRGB(j,i)&0xff;
				int p2 = gt.getRGB(j,i)&0xff;
				if(p2>200) {
					p2=255;
				}
				else{
					p2=0;
				}
				if(p1>200) {
					p1=255;
				}
				else {
					p1=0;
				}
				if(p1!=p2) {
					error++;
				}
			}
		}
		double res = (double)error/(double)pixnum;
		res*=100;
		return res;
	}

}
