package src.ddpsc.phenocv.program_execution;

import src.ddpsc.phenocv.computer_vision.ColorImage;
import src.ddpsc.phenocv.computer_vision.GrayImage;
import src.ddpsc.phenocv.utility.Lists;
import src.ddpsc.phenocv.utility.Tuple;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import org.apache.commons.io.*;

public class LoadedImages {

  public List<Tuple<ColorImage, GrayImage>> trainingImageSet;
  public List<ColorImage> processImageSet;

  public LoadedImages(String trainingDirectory, String processDirectory, String outputDirectory) {
    trainingImageSet = new Vector<Tuple<ColorImage, GrayImage>>();
    processImageSet = new Vector<ColorImage>();
    boolean trainingSuccess = loadTrainingImages(trainingDirectory);
    if(trainingSuccess) {
      System.out.println("Loaded " + trainingImageSet.size() + " pairs of training images + masks.");
    }
    boolean processSuccess = loadProcessImages(processDirectory);
    if(processSuccess) {
      System.out.println("Loaded " + processImageSet.size() + " images to process.");
    }
  }

  private boolean loadTrainingImages(String trainingDirectory) {
    boolean success = false;
    List<Tuple<String, String>> trainingImageFilenamePairs = new Vector<Tuple<String, String>>();
    File trainingDirectoryFile = new File(trainingDirectory);
    if(!trainingDirectoryFile.exists()
      && trainingDirectoryFile.isDirectory()) {
      success = false;
    } else {
      List<File> pngFiles = new Vector<File>();
      File[] allFiles = trainingDirectoryFile.listFiles();
      for(File file : allFiles) {
        String fileName = file.getName(), fileExtension = "";
        int i = fileName.lastIndexOf('.');
        if(i > 0) { fileExtension = fileName.substring(i+1); }
        if(fileExtension.contentEquals("png")) {
          pngFiles.add(file);
        }
      }
      if(pngFiles.size() > 0) {
        Collections.sort(pngFiles);
        for(int i = 0; i < pngFiles.size(); i+=2) {
          String firstFilename = pngFiles.get(i).getName(),
            secondFilename = pngFiles.get(i+1).getName(),
            imageFilename = "",
            maskFilename = "",
            firstPath = pngFiles.get(i).getPath(),
            secondPath = pngFiles.get(i+1).getPath(),
            imagePath = "",
            maskPath = "";

          if(firstFilename.contains("_mask") && !secondFilename.contains("_mask")) {
            maskFilename = firstFilename;
            imageFilename = secondFilename;
            maskPath = firstPath;
            imagePath = secondPath;
          } else if(secondFilename.contains("_mask") && !firstFilename.contains("_mask")) {
            maskFilename = secondFilename;
            imageFilename = firstFilename;
            maskPath = secondPath;
            imagePath = firstPath;
          }

          if(!imageFilename.contentEquals("") && !maskFilename.contentEquals("")) {
            String imageBaseFilename = FilenameUtils.getBaseName(imageFilename);
            String maskBaseFilename = FilenameUtils.getBaseName(maskFilename);
            int lastIndexOfUnderscore = maskBaseFilename.lastIndexOf('_');
            String maskFilenameWithoutUnderscoreMask = maskBaseFilename.substring(0, lastIndexOfUnderscore);
            if(imageBaseFilename.contentEquals(maskFilenameWithoutUnderscoreMask)) {
              trainingImageFilenamePairs.add(new Tuple<String, String>(imagePath, maskPath));
            }
          }

          if(trainingImageFilenamePairs.size() > 0) {
            trainingImageSet = Lists.loadMaskedImagePairs(trainingImageFilenamePairs);
            success = true;
          } else {
            success = false;
          }
        }
      }
    }
    return success;
  }

  private boolean loadProcessImages(String processDirectory) {
    boolean success = false;
    File processDirectoryFile = new File(processDirectory);
    if(!processDirectoryFile.exists()
      && processDirectoryFile.isDirectory()) {
      success=false;
    } else {
      List<File> pngFiles = new Vector<File>();
      File[] allFiles = processDirectoryFile.listFiles();
      for(File file : allFiles) {
        String fileName = file.getName(), fileExtension = "";
        int i = fileName.lastIndexOf('.');
        if(i > 0) { fileExtension = fileName.substring(i+1); }
        if(fileExtension.contentEquals("png")) {
          pngFiles.add(file);
        }
      }

      if(pngFiles.size() > 0) {
        List<ColorImage> colorImages = new Vector<ColorImage>();
        for(File image : pngFiles) {
          processImageSet.add(new ColorImage(image.getPath()));
        }
        success = true;
      }
    }
    return success;
  }

}
