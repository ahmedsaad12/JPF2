package com.app.jpf.interfaces;


public interface Listeners {

    interface BackListener
    {
        void back();
    }

    interface SignUpListener {

        void openSheet();

        void closeSheet();

        void checkDataValid();

        void checkReadPermission();

        void checkCameraPermission();
    }





}
