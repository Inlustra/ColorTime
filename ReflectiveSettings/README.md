# Reflective Settings

##### An annotation based Android settings fragment generator that creates 2 types of SettingsActivity

I would like to start by saying that this is purely a test, to see how far we can stretch the Preference Activity, it uses a heavy reflection and likely breaks between android versions, currently tested only on Android 5.1.1


All settings are synced with SharedPreferences, example:
* boolean → CheckBoxPreference
* String → EditTextPreference
* int/short/long → EditTextPreference with TYPE_CLASS_NUMBER 
* double/float → EditTextPreference with TYPE_CLASS_NUMBER and TYPE_NUMBER_FLAG_DECIMAL
* Date → EditTextPreference with TYPE_CLASS_DATETIME
* Enum → ListPreference (Uses Enum.toString() to set the preference title)

## Usage
```java
//Used to generate Headers and Categories depending on the activities
@SettingsHeader(title = "Time Sampler", summary = "Settings") 
public class HSVTimeSampler implements ColorSampler {

    //Applied to a boolean, creates a CheckBoxPreference
    @SettingsField(title = "Enabled", key = "hsvt_maxAmount")
    private static boolean enabled = true;
    
    //Applied to an int creates an EditTextPreference with TYPE_CLASS_NUMBER
    @SettingsField(title = "Minimum Value", summary = "Between 0 and 100, the darkest the color can be", key = "hsvt_minAmount")
    private static int minVal = 20;
    
    //Scale is an Enum and creates a ListPreference 
    @SettingsField(title = "Hue scale", summary = "Choose what the color scales on", key = "hsvt_hue") 
    private static Scale shue = Scale.Hours;
```

### Currently written Mutator types 


## Details

### SettingsHeader
<dl>
  <dt><code>String key()</code></dt>
  <dd>A key used for settings the SharedPreferences</dd>

  <dt><code>String title()</code>></dt>
  <dd>The title displayed by the SettingsPreference</dd>
  
  <dt><code>String titleId()</code></dt>
  <dd>if set, overides the title above, allows for the gathering of the title from a string resource XML</dd>
  <dd>Using: <small>context.getResources().getString</small></dd>
  <dt><code>String titleId()</code></dt>
  <dd>if set, overides the title above, should be set to a Resources Id, Example: R.strings.title</dd>
  
  <dt><code>String summary()</code></dt>
  <dd>The summary of the preference to be displayed in the SettingsPreference</dd>
  <dt><code>String summaryId()</code></dt>
  <dd>if set, overides the title above, allows for the gathering of the title from a string resource XML</dd>
  
  <dt><code>String category()</code></dt>
  <dd>Used to organize preferences within the settings panel</dd>
  <dt><code>String categoryId()</code></dt>
  <dd>if set, overides the title above, allows for the gathering of the title from a string resource XML</dd>
  
  <dt><b><code>boolean iconId()</code></b></dt>
  <dd>gets an Icon from the Android resources to be displayed left of the header</dd>
  <dt><b><code>boolean headerTop()</code></b></dt>
  <dd>Used to determine if the SettingsField annotations defined under this class are to be added to the Activity rather than a seperate menu</dd>
</dl>

### SettingsField

<dl>
  <dt><code>String key()</code></dt>
  <dd>A key used for settings the SharedPreferences</dd>

  <dt><code>String title()</code>></dt>
  <dd>The title displayed by the SettingsPreference</dd>
  
  <dt><code>String titleId()</code></dt>
  <dd>if set, overides the title above, allows for the gathering of the title from a string resource XML</dd>
  <dd>Using: <small>context.getResources().getString</small></dd>
  <dt><code>String titleId()</code></dt>
  <dd>if set, overides the title above, should be set to a Resources Id, Example: R.strings.title</dd>
  
  <dt><code>String summary()</code></dt>
  <dd>The summary of the preference to be displayed in the SettingsPreference</dd>
  <dt><code>String summaryId()</code></dt>
  <dd>if set, overides the title above, allows for the gathering of the title from a string resource XML</dd>
  
  <dt><code>String category()</code></dt>
  <dd>Used to organize preferences within the settings panel</dd>
  <dt><code>String categoryId()</code></dt>
  <dd>if set, overides the title above, allows for the gathering of the title from a string resource XML</dd>
  
  <dt><code>Class&lt;PreferenceMutator&gt; type()</code></dt>
  <dd>Allows for the overriding of the default Mutators (In order to add custom ones later)</dd>
</dl>


### ReflectiveSettingsActivity 
(WIP, Not production ready)

Creates a SettingsActivity by extending it. An example can be found in the ColorSettings project:

```java
public class ColorSettings extends ReflectiveSettingsActivity {
    @Override
    protected String getPackage() {
        return "com.thenairn.colortime"; //The package to be scanned for changes 
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //In this case, it's used to update the LiveWallpaper with the settings changes
    }
}
```



### ReflectiveHeaderedSettingsActivity 
(Mostly just a test)

Creates a SettingsActivity with Headers for tablets 
