package Thresholding;

import java.lang.*;
import java.util.*;
import java.awt.Color;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

public class Thresholding{
	
	int WIDTH_WINDOW,HEIGHT_WINDOW;
	
	BufferedImage real;
	BufferedImage Sauvola_Image;
	BufferedImage Niblack_Image;
	BufferedImage Brensen_Image;
	BufferedImage Grey_Image;
	BufferedImage Otsu_GlobalImage;
	BufferedImage Otsu_LocalImage;
	
	public Thresholding(int widthWind,int heightWind,BufferedImage original) {
		this.WIDTH_WINDOW = widthWind;
		this.HEIGHT_WINDOW = heightWind;
		this.real = original;
		this.Grey_Image=greyConvert();
		this.Sauvola_Image=null;
		this.Niblack_Image=null;
		this.Brensen_Image=null;
		this.Otsu_GlobalImage=null;
		this.Otsu_LocalImage=null;
	}
	
	private BufferedImage deepCopy() {
        ColorModel cm = Grey_Image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = Grey_Image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
	
	public static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }
	
	public BufferedImage greyConvert() { 
		int alpha, red, green, blue;
        int newPixel;
        BufferedImage lum = new BufferedImage(real.getWidth(), real.getHeight(), real.getType());
        for(int i=0; i<real.getWidth(); i++) {
            for(int j=0; j<real.getHeight(); j++) {
                alpha = new Color(real.getRGB(i, j)).getAlpha();
                red = new Color(real.getRGB(i, j)).getRed();
                green = new Color(real.getRGB(i, j)).getGreen();
                blue = new Color(real.getRGB(i, j)).getBlue();
                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
//grey level luminosity 0.21*red + 0.71*green + 0.07*blue
                newPixel = colorToRGB(alpha, red, red, red);
                lum.setRGB(i, j, newPixel);
            }
        }
        Grey_Image=lum;
        return lum;
	}
	
	public static int[] imageHistogram(BufferedImage input) {
        int[] histogram = new int[256];
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
        return histogram;
    }
	
	public static int otsuTreshold(BufferedImage original) {
        int[] histogram = imageHistogram(original);
        //Here original image given must be greyscale image or else only red threshold will be found
        long total = original.getHeight() * original.getWidth();
        double sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];
        double sumB = 0; //sum of pixel intensity till that point
        long wB = 0;     //no of pixels in back
        long wF = 0;     //no of pixels in front
        double varMax = 0;
        int threshold = 0;
        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];           //add the no of pixels on that particular point
            if(wB == 0) continue;         //if zero then add till it has a value
            wF = total - wB;              //pixels in front is pixels left to be parsed 
            if(wF == 0) break;
            sumB += (double) (i * histogram[i]);
            double mB = sumB / wB;             //mean till that point
            double mF = (sum - sumB) / wF;     //mean after that point
            double varBetween = (double) wB * (double) wF * (mB - mF) * (mB - mF);
            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
        return threshold;
    }
	
	public BufferedImage otsuLocalConvert(int size) {
		if(size>Grey_Image.getHeight()||size>Grey_Image.getWidth()) {
			int t = otsuTreshold(Grey_Image);
			return OtsuGlobal(t);
		}
		int i,j;
		BufferedImage result = new BufferedImage(Grey_Image.getWidth(),Grey_Image.getHeight(),Grey_Image.getType());
		for(i=0;i<Grey_Image.getWidth()-size;i+=size) {
			for(j=0;j<Grey_Image.getHeight()-size;j+=size) {
				BufferedImage img2 = new BufferedImage(size,size,Grey_Image.getType());
				for(int k = i;k<i+size;k++) {
					for(int l = j;l<j+size;l++) {
						img2.setRGB(k-i, l-j, Grey_Image.getRGB(k, l) );
					}
				}
				int t = otsuTreshold(img2);
				//System.out.println(t);
				for(int k = i;k<i+size;k++) {
					for(int l=j;l<j+size;l++) {
						if((Grey_Image.getRGB(k, l)&(0xff))>t) {
							int a = 255;
							int p=(a<<24)|(a<<16)|(a<<8)|(a);
							result.setRGB(k, l, p);
						}
						else {
							int a = 50;
							int p = (a<<24)|(a<<16)|(a<<8)|(a);
							result.setRGB(k, l, p);
						}
					}
				}
			}
			j-=size;
			BufferedImage img2 = new BufferedImage(size,Grey_Image.getHeight()-j,Grey_Image.getType());
			for(int k = i;k<i+size;k++) {
				for(int l = j;l<Grey_Image.getHeight();l++) {
					img2.setRGB(k-i, l-j, Grey_Image.getRGB(k, l));
				}
			}
			int t = otsuTreshold(img2);
			//System.out.println(t);
			for(int k = i;k<i+size;k++) {
				for(int l=j;l<Grey_Image.getHeight();l++) {
					if((Grey_Image.getRGB(k, l)&(0xff))>t) {
						int a = 255;
						int p=(a<<24)|(a<<16)|(a<<8)|(a);
						result.setRGB(k, l, p);
					}
					else {
						int a = 50;
						int p = (a<<24)|(a<<16)|(a<<8)|(a);
						result.setRGB(k, l, p);
					}
				}
			}
		}
		i-=size;
		for(j=0;j<Grey_Image.getHeight()-size;j+=size) {
			BufferedImage img2 = new BufferedImage(Grey_Image.getWidth()-i,size,Grey_Image.getType());
			for(int k = i;k<Grey_Image.getWidth();k++) {
				for(int l = j;l<size;l++) {
					img2.setRGB(k-i, l-j, Grey_Image.getRGB(k, l));
				}
			}
			int t = otsuTreshold(img2);
			//System.out.println(t);
			for(int k = i;k<Grey_Image.getWidth();k++) {
				for(int l=j;l<size;l++) {
					if((Grey_Image.getRGB(k, l)&(0xff))>t) {
						int a = 255;
						int p=(a<<24)|(a<<16)|(a<<8)|(a);
						result.setRGB(k, l, p);
					}
					else {
						int a = 50;
						int p = (a<<24)|(a<<16)|(a<<8)|(a);
						result.setRGB(k, l, p);
					}
				}
			}
		}
		j-=size;
		BufferedImage img2 = new BufferedImage(Grey_Image.getWidth()-i,Grey_Image.getHeight()-j,Grey_Image.getType());
		for(int k=i;k<Grey_Image.getWidth();k++) {
			for(int l = j;l<Grey_Image.getHeight();l++) {
				img2.setRGB(k-i, l-j, Grey_Image.getRGB(k, l));
			}
		}
		int t = otsuTreshold(img2);
		//System.out.println(t);
		for(int k = i;k<Grey_Image.getWidth();k++) {
			for(int l=j;l<Grey_Image.getHeight();l++) {
				if((Grey_Image.getRGB(k, l)&(0xff))>t) {
					int a = 255;
					int p=(a<<24)|(a<<16)|(a<<8)|(a);
					result.setRGB(k, l, p);
				}
				else {
					int a = 50;
					int p = (a<<24)|(a<<16)|(a<<8)|(a);
					result.setRGB(k, l, p);
				}
			}
		}
		Otsu_LocalImage=result;
		return result;
	}
	
	
	public BufferedImage OtsuGlobal(int mid) {
		if(Otsu_GlobalImage!=null) {
			return Otsu_GlobalImage;
		}
		int width = Grey_Image.getWidth();
		int height = Grey_Image.getHeight();
		BufferedImage img = new BufferedImage(width,height,Grey_Image.getType());
		for(int x=0;x<height;x++) {
			for(int y=0;y<width;y++) {
				int p = Grey_Image.getRGB(y, x);
				int a = p&0xff;
				if(a<mid) {
					a=50;
					p=(a<<24)|(a<<16)|(a<<8)|(a);
					img.setRGB(y, x, p);
				}
				else if(a>=mid) {
					a=255;
					p=(a<<24)|(a<<16)|(a<<8)|a;
					img.setRGB(y, x, p);
				}
			}
		}
		Otsu_GlobalImage=img;
		return img;
	}
	
	public BufferedImage Sauvola()
	{
		if(Sauvola_Image!=null) {
			return Sauvola_Image;
		}
		BufferedImage img = deepCopy();
		int widthImg = img.getWidth(), heightImg = img.getHeight();
		int widthWind = WIDTH_WINDOW; 
		int heightWind = HEIGHT_WINDOW; 
		int row,col, winRowStart=0, winColStart=0, winRowEnd=0, winColEnd=0;
		int meanPixel, thresh = 0, pixel;
		double standDev, coeff;
		for(row = 0; row < widthImg; row += widthWind)
		{
			for(col = 0; col < heightImg; col += heightWind) 
			{
				winRowEnd = row + widthWind;
				winColEnd = col + heightImg;
				winRowEnd = winRowEnd > widthImg ? widthImg : winRowEnd;
				winColEnd = winColEnd > heightImg ? heightImg : winColEnd;

				meanPixel = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						meanPixel += img.getRGB(winRowStart, winColStart)&0xFF;
					}
				}
				meanPixel /= ((winRowEnd-row) * (winColEnd-col));	// Caculation of Mean
				standDev = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;
						standDev += (pixel - meanPixel)*(pixel - meanPixel);
					}
				}
				standDev /= ((winRowEnd-row) * (winColEnd-col));	
				standDev = Math.sqrt(standDev);		// Calculation of Standard Deviation
				coeff = 1 + 0.5*(standDev/128 - 1);
				meanPixel*=coeff;
				thresh = meanPixel;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;

						if(pixel < thresh) {
							pixel = (0<<24) | (0<<16) | (0<<8) | 0;
						} else {
							pixel = (255<<24) | (255<<16) | (255<<8) | 255;
						}
						img.setRGB(winRowStart, winColStart, pixel);
					}
				}
			}
		}
		this.Sauvola_Image=img;
		return Sauvola_Image;
	}
	
	//Overloaded Sauvola
	public BufferedImage Sauvola(int R,double k)
	{
		if(Sauvola_Image!=null) {
			return Sauvola_Image;
		}
		BufferedImage img = deepCopy();
		int widthImg = img.getWidth(), heightImg = img.getHeight();
		int widthWind = WIDTH_WINDOW; 
		int heightWind = HEIGHT_WINDOW; 
		int row,col, winRowStart=0, winColStart=0, winRowEnd=0, winColEnd=0;
		int meanPixel, thresh = 0, pixel;
		double standDev, coeff;
		for(row = 0; row < widthImg; row += widthWind)
		{
			for(col = 0; col < heightImg; col += heightWind) 
			{
				winRowEnd = row + widthWind;
				winColEnd = col + heightImg;
				winRowEnd = winRowEnd > widthImg ? widthImg : winRowEnd;
				winColEnd = winColEnd > heightImg ? heightImg : winColEnd;

				meanPixel = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						meanPixel += img.getRGB(winRowStart, winColStart)&0xFF;
					}
				}
				meanPixel /= ((winRowEnd-row) * (winColEnd-col));	// Caculation of Mean
				standDev = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;
						standDev += (pixel - meanPixel)*(pixel - meanPixel);
					}
				}
				standDev /= ((winRowEnd-row) * (winColEnd-col));	
				standDev = Math.sqrt(standDev);		// Calculation of Standard Deviation
				coeff = 1 + k*(standDev/R - 1);
				meanPixel*=coeff;
				thresh = meanPixel;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;

						if(pixel < thresh) {
							pixel = (0<<24) | (0<<16) | (0<<8) | 0;
						} else {
							pixel = (255<<24) | (255<<16) | (255<<8) | 255;
						}
						img.setRGB(winRowStart, winColStart, pixel);
					}
				}
			}
		}
		this.Sauvola_Image=img;
		return Sauvola_Image;
	}

	public BufferedImage Niblack()
	{
		if(Niblack_Image!=null) {
			return Niblack_Image;
		}
		BufferedImage img = deepCopy();
		int widthImg = img.getWidth(), heightImg = img.getHeight();
		int widthWind = WIDTH_WINDOW; 
		int heightWind = HEIGHT_WINDOW; 
		int row,col, winRowStart=0, winColStart=0, winRowEnd=0, winColEnd=0;
		int meanPixel, thresh = 0, pixel;
		double standDev;
		for(row = 0; row < widthImg; row += widthWind)
		{
			for(col = 0; col < heightImg; col += heightWind) 
			{
				winRowEnd = row + widthWind;
				winColEnd = col + heightImg;
				winRowEnd = winRowEnd > widthImg ? widthImg : winRowEnd;
				winColEnd = winColEnd > heightImg ? heightImg : winColEnd;
				meanPixel = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						meanPixel += img.getRGB(winRowStart, winColStart)&0xFF;
					}
				}
				meanPixel /= ((winRowEnd-row) * (winColEnd-col));	// Caculation of Mean
				standDev = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;
						standDev += (pixel - meanPixel)*(pixel - meanPixel);
					}
				}
				standDev /= ((winRowEnd-row) * (winColEnd-col));	
				standDev = Math.sqrt(standDev);		// Calculation of Standard Deviation
				thresh = meanPixel + ((int)(-150*standDev))/100;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;

						if(pixel < thresh) {
							pixel = (0<<24) | (0<<16) | (0<<8) | 0;
						} else {
							pixel = (255<<24) | (255<<16) | (255<<8) | 255;
						}
						img.setRGB(winRowStart, winColStart, pixel);
					}
				}
			}
		}
		this.Niblack_Image=img;
		return img;
	}
	
	//Overloaded Niblack
	public BufferedImage Niblack(double k)
	{
		if(Niblack_Image!=null) {
			return Niblack_Image;
		}
		BufferedImage img = deepCopy();
		int widthImg = img.getWidth(), heightImg = img.getHeight();
		int widthWind = WIDTH_WINDOW; 
		int heightWind = HEIGHT_WINDOW; 
		int row,col, winRowStart=0, winColStart=0, winRowEnd=0, winColEnd=0;
		int meanPixel, thresh = 0, pixel;
		double standDev;
		for(row = 0; row < widthImg; row += widthWind)
		{
			for(col = 0; col < heightImg; col += heightWind) 
			{
				winRowEnd = row + widthWind;
				winColEnd = col + heightImg;
				winRowEnd = winRowEnd > widthImg ? widthImg : winRowEnd;
				winColEnd = winColEnd > heightImg ? heightImg : winColEnd;
				meanPixel = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						meanPixel += img.getRGB(winRowStart, winColStart)&0xFF;
					}
				}
				meanPixel /= ((winRowEnd-row) * (winColEnd-col));	// Caculation of Mean
				standDev = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;
						standDev += (pixel - meanPixel)*(pixel - meanPixel);
					}
				}
				standDev /= ((winRowEnd-row) * (winColEnd-col));	
				standDev = Math.sqrt(standDev);		// Calculation of Standard Deviation
				thresh = meanPixel + ((int)(k*standDev))/100;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;

						if(pixel < thresh) {
							pixel = (0<<24) | (0<<16) | (0<<8) | 0;
						} else {
							pixel = (255<<24) | (255<<16) | (255<<8) | 255;
						}
						img.setRGB(winRowStart, winColStart, pixel);
					}
				}
			}
		}
		this.Niblack_Image=img;
		return img;
	}


	public BufferedImage Brensen()
	{
		if(Brensen_Image!=null) {
			return Brensen_Image;
		}
		BufferedImage img = deepCopy();
		int widthImg = img.getWidth(), heightImg = img.getHeight();
		int widthWind = WIDTH_WINDOW; 
		int heightWind = HEIGHT_WINDOW; 
		int row,col, winRowStart=0, winColStart=0, winRowEnd=0, winColEnd=0;
		int minPixel, maxPixel, thresh, pixel;
		for(row = 0; row < widthImg; row += widthWind)
		{
			for(col = 0; col < heightImg; col += heightWind) 
			{
				winRowEnd = row + widthWind;
				winColEnd = col + heightImg;
				winRowEnd = winRowEnd > widthImg ? widthImg : winRowEnd;
				winColEnd = winColEnd > heightImg ? heightImg : winColEnd;
				minPixel = 255; maxPixel = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;
						minPixel = minPixel > pixel ? pixel : minPixel;
						maxPixel = maxPixel < pixel ? pixel : maxPixel;
					}
				}
				thresh = (minPixel + maxPixel)/2;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;

						if(pixel < thresh) {
							pixel = (0<<24) | (0<<16) | (0<<8) | 0;
						} else {
							pixel = (255<<24) | (255<<16) | (255<<8) | 255;
						}
						img.setRGB(winRowStart, winColStart, pixel);
					}
				}
			}
		}
		Brensen_Image=img;
		return img;
	}
	
	/*public static void main(String[] args)throws IOException {
		BufferedImage img = null;
		File f = null;
		f=new File("Datasets/H05.jpg");
		img = ImageIO.read(f);
		f=new File("Outputs/H5/Original.jpg");
		ImageIO.write(img,"jpg",f);
		Thresholding th = new Thresholding(20,20,img);
		BufferedImage img2 = th.greyConvert();
		f=new File("Outputs/H5/Grey.jpg");
		ImageIO.write(img2,"jpg",f);
		img2=th.OtsuGlobal(otsuTreshold(img));
		f=new File("Outputs/H5/OtsuGlobal.jpg");
		ImageIO.write(img2,"jpg",f);
		img2=th.otsuLocalConvert(350);
		f=new File("Outputs/H5/OtsuLocal.jpg");
		ImageIO.write(img2,"jpg",f);
		img2=th.Sauvola();
		f=new File("Outputs/H5/Sauvola.jpg");
		ImageIO.write(img2,"jpg",f);
		img2=th.Niblack();
		f=new File("Outputs/H5/Niblack.jpg");
		ImageIO.write(img2,"jpg",f);
		img2=th.Brensen();
		f=new File("Outputs/H5/Brensen.jpg");
		ImageIO.write(img2,"jpg",f);
		System.out.println("Done! The Awesome Outputs are in Outputs Folder.");
	}*/
}