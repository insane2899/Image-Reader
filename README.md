# Image-Reader
This is a project made in Java 11 and Swing. 
The idea of the project came to me as I had image binarization as a part of my college 2nd Year Project. While reviewing the 
different techniques by sauvola and niblack and others I faced severe trouble owing to the lack of any application which will 
generate the binarized images according to input value (atleast I don't know any such application and I searched a lot 
<bold><strong>(A LOT)</strong></bold>)
Anyway to use this project one must have <strong>Tesseract OCR</strong> installed and added into Java IDE you use.
Now about the project:

On running the project the first page that comes is:
![Screenshot](https://github.com/insane2899/Image-Reader/blob/master/userguide_images/landing.png?raw=true "Start Image")

On click Enter a dialogue box appears asking to enter path to the image to be edited:

![Screenshot](https://github.com/insane2899/Image-Reader/blob/master/userguide_images/read.png?raw=true "Reading Image")

On entering the path the image and a workbench opens in two different frames. The image is scaled down to appropriate size to fit and compare.

![Screenshot](https://github.com/insane2899/Image-Reader/blob/master/userguide_images/open.png?raw=true "Opening Image")

On choosing the binarization method and technique and pressing the button the binarized image opens in another frame. Binarization might take about 1 minute so wait patiently.

![Screenshot](https://github.com/insane2899/Image-Reader/blob/master/userguide_images/start.png?raw=true "Binarized Image")

On the binarization frame the custom button allows to input some user defined parameters in 3 techniques:
<ul>
  <li>Sauvola Binarization</l1>
  <li>Niblack Binarization</li>
  <li>Otsu's Local Binarization</li>
</ul>

On entering the values another frame with custom binarization appears:

![Screenshot](https://github.com/insane2899/Image-Reader/blob/master/userguide_images/convert.png?raw=true "Custom Image")

![Screenshot](https://github.com/insane2899/Image-Reader/blob/master/userguide_images/donno.png?raw=true "Custom Image")

To save image the save button needs to be pressed. Saving is a hardcoded method that saves the file in a premade 'Results' folder under the image name.

The compare button compares two binarizations or a binarization and a ground truth and tells the percentage devaiation.

![Screenshot](https://github.com/insane2899/Image-Reader/blob/master/userguide_images/save.png?raw=true "Compare Image")

![Screenshot](https://github.com/insane2899/Image-Reader/blob/master/userguide_images/edit.png?raw=true "Compare Image")

The Convert Image button converts the image in a text and displays it in a frame. This convert is done by Tesseract OCR and is good for printed document images but not good enough for handwritten documents.

![Screenshot](https://github.com/insane2899/Image-Reader/blob/master/userguide_images/tired.png?raw=true "Translate Image")

The final method is the converted image process in the workbench. On selecting the format and the designated folder the image gets converted to the selected format and saved in the given folder.

The project will have some updates in the future, specially in the look-and-feel part.
  


