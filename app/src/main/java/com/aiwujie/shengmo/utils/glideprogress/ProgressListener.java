package com.aiwujie.shengmo.utils.glideprogress;

/**
 * Created by Alexey on 06.12.15.
 */
public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}