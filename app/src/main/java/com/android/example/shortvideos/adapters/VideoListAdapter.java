package com.android.example.shortvideos.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.example.shortvideos.MyApplication;
import com.android.example.shortvideos.databinding.VideoRecyclerItemBinding;
import com.android.example.shortvideos.models.MediaData;
import com.android.example.shortvideos.util.CacheUtil;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListAdapter extends
        ListAdapter<MediaData, VideoListAdapter.ViewHolder> {


    public VideoListAdapter(@NonNull DiffUtil.ItemCallback<MediaData> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaData data = getItem(position);
        try {
            holder.onBind(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.initializePlayer();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.stopPlayer();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final VideoRecyclerItemBinding dataBinding;
        private SimpleExoPlayer exoPlayer;
        private DataSource.Factory cacheDataSourceFactory;
        private MediaSource mediaSource ;

        private ViewHolder(@NonNull VideoRecyclerItemBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
        }

        protected void onBind(MediaData data) throws IOException {
            final Context context= dataBinding.getRoot().getContext().getApplicationContext();

            Uri uri = Uri.parse(data.getVideo());
            MediaItem item = MediaItem.fromUri(uri);

            CacheUtil.getInstance().preCacheVideo(uri,context);

            HttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory()
                    .setAllowCrossProtocolRedirects(true);

            cacheDataSourceFactory = new CacheDataSource.Factory()
                    .setCache(MyApplication.simpleCache)
                    .setUpstreamDataSourceFactory(httpDataSourceFactory)
                    .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);

            mediaSource = new ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                    .createMediaSource(item);
        }

        protected void initializePlayer() {
            Context context = dataBinding.getRoot().getContext().getApplicationContext();

            exoPlayer = new SimpleExoPlayer.Builder(context)
                    .setMediaSourceFactory(new DefaultMediaSourceFactory(cacheDataSourceFactory))
                    .build();

            dataBinding.videoView.setPlayer(exoPlayer);
            exoPlayer.setMediaSource(mediaSource,true);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.prepare();
            exoPlayer.play();
        }

        protected void stopPlayer() {
            exoPlayer.stop();
            exoPlayer.release();
        }


        public static ViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            VideoRecyclerItemBinding dataBinding = VideoRecyclerItemBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(dataBinding);
        }
    }

    public static class MediaDataDiffUtil extends DiffUtil.ItemCallback<MediaData> {

        @Override
        public boolean areItemsTheSame(@NonNull MediaData oldItem, @NonNull MediaData newItem) {
            return oldItem.get_id().equals(newItem.get_id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MediaData oldItem, @NonNull MediaData newItem) {
            return oldItem.equals(newItem);
        }
    }


}
