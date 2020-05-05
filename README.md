# OwnTracks Log

This repository, when configured to run at startup on an internet-configured Windows computer, scrapes [GeoJSON](https://geojson.org/) location data uploaded to [Adafruit IO](https://io.adafruit.com/) from an [OwnTracks](https://github.com/owntracks/owntracks) source. This data is then converted into two legible files, *owntracksLog.txt* and *owntracksLog.csv*.

Running at startup is an optional matter of convenience, detailed below since Adafruit IO clears its feeds of old data. Periodically running *owntracks.bat* serves the same general purpose.

## Install instructions:

1. Clone or download the repository.
   - Take note of where you place this repository. The address you choose is your **[DIR]**, which should end in `\owntracksLog` if you haven't changed the folder name.
2. Configure Adafruit IO.
   - Create an account with the site, create then a new Adafruit IO feed with a name of your choosing (accessible via "Feeds > Actions > Create a New Feed").
   - Open the feed page and click "Feed Info" (right-hand side). Copy the API Endpoint URL, then use a text editor to add `/data` to the end. This is your **[URL]**.
   - On the feed page, click "Adafruit IO Key" (top-right). Copy the Active Key shown to your text editor. This is your **[KEY]**.
3. Configure the executable batch file.
   - Open up *owntracksLog.bat* in your text editor. This is what should appear:
  ```
  curl -H "X-AIO-Key:[KEY]" [URL] >>owntracksLog.txt
  start "[DIR]" owntracksLogPrune.jar
  ```
3. (cont.)
   - Replace **[DIR]**, **[URL]**, and **[KEY]** with their respective values. Save and close the file.
   - OPTIONAL: Follow [this guide](https://www.freecodecamp.org/news/how-to-change-startup-program-in-windows-7-8-and-10/) to add *owntracksLog.bat* as a startup file on your computer. Remember to use a shortcut to *owntracksLog.bat*, rather than the file itself.
4. Configure your OwnTracks tracker of choice.
   - Configuration varies from tracker to tracker, so here's a list of all components needed to set up a connection to Adafruit IO. This list is based off the [Android app's](https://github.com/owntracks/android) connection configuration page:
     - Mode: `MQTT`
     - Host: `io.adafruit.com`, port `1833`. If given the option, don't use WebSockets.
     - Identification: Username is the same as your Adafruit IO username, password is your **[KEY]**. If given the option, set a Device ID of your choosing.
     - Security: If enabled, disable `TLS`.
     - Parameters: None.
   - Check to see if you're able to upload location data from your tracker to the Adafruit IO feed page. Run *owntracksLog.bat* whenever you'd like to scrape this data to your computer. Previously-scraped and new data should be accessible via *owntracksLog.txt* (a text file) and *owntracksLog.csv* (a spreadsheet file). Of note, *owntracksLog.csv* only contains Latitude, Longitute, Date, and Time data for each tracked location.
