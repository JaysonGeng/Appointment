package com.example.appointment.Dao;

import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;



public class OnClickView {

    public static void click_small(final View former_view,final View latter_view){

        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(500);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f,2f,
                1f,2f,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0);
        scaleAnimation.setDuration(500);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,-0.5f,
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,-0.5f);
        translateAnimation.setDuration(500);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        former_view.startAnimation(animationSet);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                former_view.setVisibility(View.GONE);
                latter_view.setVisibility(View.VISIBLE);
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0f,1f);
                alphaAnimation.setDuration(500);
                ScaleAnimation scaleAnimation = new ScaleAnimation(
                        1.2f,1f,
                        1.2f,1f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,0f);
                scaleAnimation.setDuration(500);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF,-0.1f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,-0.1f,
                        Animation.RELATIVE_TO_SELF,0f);
                translateAnimation.setDuration(500);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(translateAnimation);
                latter_view.startAnimation(animationSet);
            }
        },500);
    }


    public static void click_big(final View view){
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f,1.2f);
        alphaAnimation.setDuration(600);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.05f,0.9f,
                1.05f,0.9f,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0);
        scaleAnimation.setDuration(600);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,-0.025f,
                Animation.RELATIVE_TO_SELF,0.05f,
                Animation.RELATIVE_TO_SELF,-0.025f,
                Animation.RELATIVE_TO_SELF,0.05f);
        translateAnimation.setDuration(600);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        view.startAnimation(animationSet);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.2f,1.2f);
                alphaAnimation.setDuration(100);
                ScaleAnimation scaleAnimation = new ScaleAnimation(
                        0.9f,0.9f,
                        0.9f,0.9f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,0f);
                scaleAnimation.setDuration(100);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF,0.05f,
                        Animation.RELATIVE_TO_SELF,0.05f,
                        Animation.RELATIVE_TO_SELF,0.05f,
                        Animation.RELATIVE_TO_SELF,0.05f);
                translateAnimation.setDuration(100);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(translateAnimation);
                view.startAnimation(animationSet);
            }
        },600);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0.2f);
                alphaAnimation.setDuration(200);
                ScaleAnimation scaleAnimation = new ScaleAnimation(
                        0.9f,1.1f,
                        0.9f,1.1f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,0f);
                scaleAnimation.setDuration(200);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF,0.05f,
                        Animation.RELATIVE_TO_SELF,-0.05f,
                        Animation.RELATIVE_TO_SELF,0.05f,
                        Animation.RELATIVE_TO_SELF,-0.05f);
                translateAnimation.setDuration(200);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(translateAnimation);
                view.startAnimation(animationSet);
            }
        },700);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f,0f);
                alphaAnimation.setDuration(600);
                ScaleAnimation scaleAnimation = new ScaleAnimation(
                        1.1f,1.2f,
                        1.1f,1.2f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,0f);
                scaleAnimation.setDuration(600);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF,-0.05f,
                        Animation.RELATIVE_TO_SELF,-0.1f,
                        Animation.RELATIVE_TO_SELF,-0.05f,
                        Animation.RELATIVE_TO_SELF,-0.1f);
                translateAnimation.setDuration(600);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(translateAnimation);
                view.startAnimation(animationSet);
            }
        },900);
    }

    public static void logo(final View view){
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f,1f);
        alphaAnimation.setDuration(600);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.5f,1f,
                0.5f,1f,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0);
        scaleAnimation.setDuration(600);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0.25f,
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0.25f,
                Animation.RELATIVE_TO_SELF,0f);
        translateAnimation.setDuration(600);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        view.startAnimation(animationSet);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0.8f);
                alphaAnimation.setDuration(1000);
                ScaleAnimation scaleAnimation = new ScaleAnimation(
                        1f,1f,
                        1f,1f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,0f);
                scaleAnimation.setDuration(1000);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,0f);
                translateAnimation.setDuration(1000);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(translateAnimation);
                view.startAnimation(animationSet);
            }
        },600);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.8f,0f);
                alphaAnimation.setDuration(1400);
                ScaleAnimation scaleAnimation = new ScaleAnimation(
                        1f,1.4f,
                        1f,1.4f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,0f);
                scaleAnimation.setDuration(1400);
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,-0.2f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,-0.2f);
                translateAnimation.setDuration(1400);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(translateAnimation);
                view.startAnimation(animationSet);
            }
        },1600);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0f,1f);
                alphaAnimation.setDuration(1000);
                ScaleAnimation scaleAnimation = new ScaleAnimation(
                        1f,1f,
                        1f,1f,
                        Animation.RELATIVE_TO_SELF,0f,
                        Animation.RELATIVE_TO_SELF,0f);
                scaleAnimation.setDuration(1000);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(scaleAnimation);
                view.startAnimation(animationSet);
            }
        },3000);
    }
}
