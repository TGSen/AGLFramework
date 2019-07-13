package com.aglframework.smzh;

public interface IFilter {

    Frame draw(Frame frame);

    void destroy();

    class Frame {
        private int textureId;
        private int textureWidth;
        private int textureHeight;

        public Frame(int textureId, int textureWidth, int textureHeight) {
            this.textureId = textureId;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }

        public int getTextureId() {
            return textureId;
        }

        public int getTextureWidth() {
            return textureWidth;
        }

        public int getTextureHeight() {
            return textureHeight;
        }
    }

}