#PhenoCV

Java OpenCV wrapper for performing visual analysis on images of plants.


##Description
This software is a small API built on top of the Open CV java bindings (http://opencv.org/opencv-java-api.html) used to isolate images of plants from their background and run a variety of visual analysis algorithms upon the isolated plant portion of the image.

This application generally behaves by:
* Taking in a set training data and using the data to build a model of the system
* Selecting the portions of the plant that match the supplied model (or don't match, depending on the algorithm)
* Saving a new file with only the selected plant visible in the image


##Assumptions
The OpenCV Java bindings used in building are of version 2.4.9 and depend on the structure of the 2.4.9 codebase. Ensure that any future versions used are compatible with OpenCV 2.4.9. Included in the \lib folder are the windows specific 2.4.9 Java binding .dlls and .jar file. If the system isn't recognizing the OpenCV classes, ensure \lib is included in your java build path.

The developers built this on a Windows system and so used the Windows version of OpenCV. Building on another operating system should amount to ensuring the appropriate compiled binaries and Java .jar bindings of OpenCV are in the build path. See the \lib folder for the required elements. Also see: (http://opencv.org/downloads.html)

This project was developed with Apache Maven (http://maven.apache.org/) and to build you should ensure that pom.xml is correctly recognized by your IDE. If all else fails, the only dependency other than OpenCV is JUnit 4.10 (http://sourceforge.net/projects/junit/files/junit/4.10/). If Maven isn't behaving, this can be installed and included manually and everything should work after that.


###Algorithms
Here we discuss the implementation of each algorithm.

####Color Isolation
This algorithm is built primarily on top of histogram backprojection.

The algorithm assumes the plants will be uniquely colored in the image. So if anything in the background is the same color as the plant, it will be prone to being included in the isolation and throw off the results. The more isolate the plant is, the better the results.

* A set of paired training images are input into the system when the program is initialized.
  - These paired images are sample images of the plants paired with masks revealing only the plant-parts in the images
  - The training images work best when they are of the same species (or just color) as the plants you want to isolate from the background
  - Examples of training masks can be found in \resources\images\color isolation images, indeed the image-mask pairs in that folder are the default settings for the algorithm
  - To make your own training data, use your favorite image editor and paint over the plant white, and everything else black. Remember to apply a threshold filter (so the images don't have any shades of gray) before saving the images otherwise you may get unexpected results.
  - ~20 is a good number of test images, absolutely have more than 5.
* A HSV (http://en.wikipedia.org/wiki/HSL_and_HSV) histogram model is built on top of the manually isolated training images
* The input images to process are then graded based on the probability than any one of their pixels are in the histogram.
* Any pixel with an above 0% probability are included in the intermediate mask
* This mask is smoothed to remove noise and stray pixels, as well as close small gaps in the selected areas
* Automatic contour finding is applied to the smoothed mask and only the green-ish contours are kept. All others are discarded.
* The remaining contours (if any) are the plant.

If the output result of this algorithm is unacceptable, go back and adjust the training images. If portions of a plant are being regularly excluded, include more plants in the training set with those features included. If a portion of the background is being included when it shouldn't be, go through the training images and ensure the mask doesn't leave the surface of the plants and that the images don't have any plant-colored background in them.

This algorithm is optimized for easy, comprehensible modification, and dynamic backgrounds that aren't always the same.

###Background Modeling
Incomplete. Future feature.

###Testing
Large portions of the project have built in test methods. However, due to the nature of visual computations, most of the tests can't be Assert-pass-fail automated tests. Instead, they write images to the filesystem in the \resources\images directory. Confirmation that the tests worked depend upon viewing the images themselves and knowing what they should look like.

Some of the images' appearances can be deduced from their names, others require knowledge of the tests themselves. To that end, the tests are really more for developers than users.

Be warned, some of the tests output hundreds of megabytes of data. Particularly the tests in the package src.ddpsc.phenocv.algorithm_test.

###Run
Not implemented. Currently there is no entry point to the application, only the JUnit tests.

###License
Licensed under GNU-GPL v3.0. (http://www.gnu.org/copyleft/gpl.html)

The full license can be found in license.txt.