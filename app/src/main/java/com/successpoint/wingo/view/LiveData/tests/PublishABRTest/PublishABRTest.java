//
// Copyright © 2015 Infrared5, Inc. All rights reserved.
//
// The accompanying code comprising examples for use solely in conjunction with Red5 Pro (the "Example Code")
// is  licensed  to  you  by  Infrared5  Inc.  in  consideration  of  your  agreement  to  the  following
// license terms  and  conditions.  Access,  use,  modification,  or  redistribution  of  the  accompanying
// code  constitutes your acceptance of the following license terms and conditions.
//
// Permission is hereby granted, free of charge, to you to use the Example Code and associated documentation
// files (collectively, the "Software") without restriction, including without limitation the rights to use,
// copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The Software shall be used solely in conjunction with Red5 Pro. Red5 Pro is licensed under a separate end
// user  license  agreement  (the  "EULA"),  which  must  be  executed  with  Infrared5,  Inc.
// An  example  of  the EULA can be found on our website at: https://account.red5pro.com/assets/LICENSE.txt.
//
// The above copyright notice and this license shall be included in all copies or portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,  INCLUDING  BUT
// NOT  LIMITED  TO  THE  WARRANTIES  OF  MERCHANTABILITY, FITNESS  FOR  A  PARTICULAR  PURPOSE  AND
// NONINFRINGEMENT.   IN  NO  EVENT  SHALL INFRARED5, INC. BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN  AN  ACTION  OF  CONTRACT,  TORT  OR  OTHERWISE,  ARISING  FROM,  OUT  OF  OR  IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.successpoint.wingo.view.LiveData.tests.PublishABRTest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.event.R5ConnectionEvent;
import com.red5pro.streaming.source.R5AdaptiveBitrateController;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;
import com.red5pro.streaming.view.R5VideoView;

import com.successpoint.wingo.R;
import com.successpoint.wingo.view.LiveData.tests.PublishTest.PublishTest;
import com.successpoint.wingo.view.showLiveView.TestContent;

/**
 * Created by davidHeimann on 2/10/16.
 */
public class PublishABRTest extends PublishTest {

    protected R5AdaptiveBitrateController adaptor;

    @Override
    public void onConnectionEvent(R5ConnectionEvent event) {
        super.onConnectionEvent(event);
        if (event.name() == R5ConnectionEvent.ABR_LEVEL_CHANGED.name()) {
            int level = adaptor.getBitRateLevel();
            if (level >= 0) {
                int bitrate = adaptor.getBitrateLevelValues()[level];
                Log.d("Publisher", "ABR Level Change: level(" + level + "), bitrate(" + bitrate + ")");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.publish_test, container, false);

        //Create the configuration from the values.xml
        R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP,
                TestContent.GetPropertyString("host"),
                TestContent.GetPropertyInt("port"),
                TestContent.GetPropertyString("context"),
                TestContent.GetPropertyFloat("publish_buffer_time"));
        config.setLicenseKey(TestContent.GetPropertyString("license_key"));
        config.setBundleID(getActivity().getPackageName());

        R5Connection connection = new R5Connection(config);

        //setup a new stream using the connection
        publish = new R5Stream(connection);
        publish.audioController.sampleRate =  TestContent.GetPropertyInt("sample_rate");

        publish.setListener(this);

        //show all logging
        publish.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

        R5Camera camera = null;
        if(TestContent.GetPropertyBool("video_on")) {
            //attach a camera video source
            cam = openFrontFacingCameraGingerbread();
            cam.setDisplayOrientation((camOrientation + 180) % 360);

            camera = new R5Camera(cam, TestContent.GetPropertyInt("camera_width"), TestContent.GetPropertyInt("camera_height"));
            camera.setBitrate(TestContent.GetPropertyInt("bitrate"));
            camera.setOrientation(camOrientation);
        }

        if(TestContent.GetPropertyBool("audio_on")) {
            //attach a microphone
            R5Microphone mic = new R5Microphone();
            publish.attachMic(mic);
        }

        preview = (R5VideoView)rootView.findViewById(R.id.videoPreview);

        preview.attachStream(publish);

        if(TestContent.GetPropertyBool("video_on"))
            publish.attachCamera(camera);

        preview.showDebugView(TestContent.GetPropertyBool("debug_view"));

        adaptor = new R5AdaptiveBitrateController();
        adaptor.AttachStream(publish);
        adaptor.requiresVideo = false;

        publish.publish(TestContent.GetPropertyString("stream1"), getPublishRecordType());

        if(TestContent.GetPropertyBool("video_on"))
            cam.startPreview();

        return rootView;
    }
}
