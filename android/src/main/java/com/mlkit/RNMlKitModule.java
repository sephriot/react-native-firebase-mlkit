
package com.mlkit;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class RNMlKitModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private TextRecognizer textRecognizer;

  public RNMlKitModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @ReactMethod
  public void deviceTextRecognition(String uri, final Promise promise) {
      try {
          InputImage image = InputImage.fromFilePath(this.reactContext, android.net.Uri.parse(uri));
          TextRecognizer recognizer = this.getTextRecognizerInstance();
          Task<FirebaseVisionText> result =
                  recognizer.processImage(image)
                          .addOnSuccessListener(new OnSuccessListener<Text>() {
                              @Override
                              public void onSuccess(Text visionText) {
                                  promise.resolve(visionText);
                              }
                          })
                          .addOnFailureListener(
                                  new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull Exception e) {
                                          e.printStackTrace();
                                          promise.reject(e);
                                      }
                                  });;
      } catch (IOException e) {
          promise.reject(e);
          e.printStackTrace();
      }
  }

  private FirebaseVisionTextRecognizer getTextRecognizerInstance() {
    if (this.textDetector == null) {
      this.textDetector = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }
    return this.textDetector;
  }

  @Override
  public String getName() {
    return "RNMlKit";
  }
}
