// Extracts PiP tracking information to a CSV file
//
// Works with Vegas 17
//
// Based on this script: https://www.vegascreativesoftware.info/us/forum/beta-pin-image-video-to-motion-track--112837/?page=2
// With the help of this documentation: https://walkedby.com/other/VEGASScriptAPI.html
//
// How to Use: (Remember, this is a very hacky script!)
// 1. Select the track event
// 2. Run the script
// 3. Yay!
//
// Don't forget to change the file name of the CSV file!
//
// Made by MrPowerGamerBR
using System.Collections.Generic;
using System.Windows.Forms;
using ScriptPortal.Vegas;
using System.IO;

public class EntryPoint {
    Vegas myVegas;

    public void FromVegas(Vegas vegas) {
        myVegas = vegas;
        
        List<TrackEvent> trackEvents = FindSelectedEvents(myVegas.Project);

        MessageBox.Show("There are " + trackEvents.Count + " events selected!");

        foreach (var trackEventX in trackEvents) {
            var trackEvent = trackEventX as VideoEvent;

            Timecode trackStart = trackEvent.Start;
            Timecode trackLength = trackEvent.Length;
            Timecode trackEnd = trackEvent.End;

            List<Effect> fxList = new List<Effect>(trackEvent.Effects);

            Effect pipFX = fxList.Find(x => (x.PlugIn.UniqueID == "{Svfx:com.vegascreativesoftware:pictureinpicture}"));
            bool hasPipFx = pipFX != null;

            // var csv = "Frames;LocationX;LocationY;Angle;Scale;DistortionScaleY;Shear;Mode";
            var csv = "";

            if (hasPipFx) {
                // MessageBox.Show("Has Pip FX!");

                List<OFXParameter> ofxList = new List<OFXParameter>(pipFX.OFXEffect.Parameters);

                // MessageBox.Show("Object Type: " + ofx.GetType().Name);

                foreach (var ofx in ofxList) {
                    // MessageBox.Show("Key: " + ofx.Name + " - " + ofx.GetType().Name);
                }

                OFXDouble2DParameter locationParam = (OFXDouble2DParameter) ofxList.Find(x => (x.Name == "Location"));
                OFXDoubleParameter angleParam = (OFXDoubleParameter) ofxList.Find(x => (x.Name == "Angle"));
                OFXDoubleParameter scaleParam = (OFXDoubleParameter) ofxList.Find(x => (x.Name == "Scale"));
                // Doesn't seem to exist?
                // OFXDoubleParameter distortionScaleX = (OFXDoubleParameter) ofxList.Find(x => (x.Name == "DistortionScaleX"));
                OFXDoubleParameter distortionScaleY = (OFXDoubleParameter) ofxList.Find(x => (x.Name == "DistortionScaleY"));
                OFXDoubleParameter distortionShear = (OFXDoubleParameter) ofxList.Find(x => (x.Name == "DistortionShear"));
                OFXChoiceParameter keepProportions = (OFXChoiceParameter) ofxList.Find(x => (x.Name == "KeepProportions"));

                Timecode currentTimecode = Timecode.FromFrames(0);

                var isFirst = true;

                while (trackLength >= currentTimecode) {
                    // MessageBox.Show("Value: " + currentTimecode + "; " + pipLocation.GetValueAtTime(currentTimecode).X + "; " + pipLocation.GetValueAtTime(currentTimecode).Y);
                    if (!isFirst)
                        csv += "\n";

                    csv += ("" + (trackStart.FrameCount + currentTimecode.FrameCount));
                    csv += ";";
                    csv += ("" + locationParam.GetValueAtTime(currentTimecode).X);
                    csv += ";";
                    csv += ("" + locationParam.GetValueAtTime(currentTimecode).Y);
                    csv += ";";
                    csv += ("" + angleParam.GetValueAtTime(currentTimecode));
                    csv += ";";
                    csv += ("" + scaleParam.GetValueAtTime(currentTimecode));
                    csv += ";";
                    csv += ("" + distortionScaleY.GetValueAtTime(currentTimecode));
                    csv += ";";
                    csv += ("" + distortionShear.GetValueAtTime(currentTimecode));
                    csv += ";";
                    csv += ("" + keepProportions.Value);
                    isFirst = false;
                    
                    currentTimecode = currentTimecode + Timecode.FromFrames(1);
                }

                /* foreach (var keyframe in pipLocation.Keyframes) {
                    MessageBox.Show("Key: " + keyframe);
                    MessageBox.Show("Key: " + keyframe.Value);
                    MessageBox.Show("Key: " + keyframe.Value.X + " ; "+  keyframe.Value.Y);
                } */

            
                using(StreamWriter writetext = File.AppendText("L:\\LorittaAssets\\chaves\\keyframes.txt")) {
                    writetext.WriteLine(csv);
                }

            } else {
                MessageBox.Show("Missing Pip FX...");
            }
        }

        MessageBox.Show("Done!");
    }

    /// <summary>
    /// Returns the first selected event that's under the cursor
    /// </summary>
    /// <returns>The first selected event or null if no event selected</returns>
    List<TrackEvent> FindAllSelectedEventsUnderCursor() {
        List<TrackEvent> selectedEventsAcrossTracks = new List<TrackEvent>();

        foreach (Track track in myVegas.Project.Tracks)
        {
            foreach (TrackEvent trackEvent in track.Events)
            {
                if (trackEvent.Selected && trackEvent.Start <= myVegas.Transport.CursorPosition && trackEvent.End >= myVegas.Transport.CursorPosition)
                {
                    selectedEventsAcrossTracks.Add(trackEvent);
                }
            }
        }

        return selectedEventsAcrossTracks;
    }

    List<TrackEvent> FindSelectedEvents(Project project) {
        List<TrackEvent> selectedEvents = new List<TrackEvent>();
        foreach (Track track in project.Tracks) {
            foreach (TrackEvent trackEvent in track.Events) {
                if (trackEvent.Selected) {
                    selectedEvents.Add(trackEvent);
                }
            }
        }
        return selectedEvents;
    }
}
