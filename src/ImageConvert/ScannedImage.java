package ImageConvert;

import java.awt.image.BufferedImage;
import net.sourceforge.tess4j.Tesseract; 
import net.sourceforge.tess4j.TesseractException; 
  
public class ScannedImage { 
    public String convert(BufferedImage img)
    { 
        Tesseract tesseract = new Tesseract(); 
        try { 
  
            tesseract.setDatapath("/home/soumik/Desktop/Java/Libraries/Tess4J-3.4.8-src/Tess4J/tessdata"); 
  
            // the path of your tess data folder 
            // inside the extracted file 
            String text 
                = tesseract.doOCR(img); 
  
            // path of your image file 
            return text;
        } 
        catch (TesseractException e) { 
            e.printStackTrace(); 
            return null;
        } 
    } 
}