package com.android.example.shortvideos.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.example.shortvideos.databinding.VideoRecyclerItemBinding;
import com.android.example.shortvideos.models.MediaData;
import com.android.example.shortvideos.util.CacheUtil;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;

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
        holder.onBind(data);
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
        private MediaSource mediaSource;

        private ViewHolder(@NonNull VideoRecyclerItemBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
        }

        public static ViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            VideoRecyclerItemBinding dataBinding = VideoRecyclerItemBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(dataBinding);
        }

        protected void onBind(MediaData data) {

            final Context context = dataBinding.getRoot().getContext().getApplicationContext();

            Uri uri = Uri.parse(data.getVideo());
            MediaItem item = new MediaItem.Builder().setUri(uri).setCustomCacheKey(uri.toString()).build();

            CacheUtil cacheUtil = CacheUtil.getInstance();
            cacheUtil.preCacheVideo(uri, context);

            cacheDataSourceFactory = new CacheDataSource.Factory()
                    .setCache(cacheUtil.getSimpleCache())
                    .setUpstreamDataSourceFactory(cacheUtil.getHttpDataSourceFactory());

            mediaSource = new ProgressiveMediaSource
                    .Factory(cacheDataSourceFactory)
                    .createMediaSource(item);

            setUpExoPlayer();

            String profileUrl = data.getUser_info().getProfile_pic();

            Glide.with(dataBinding.profileImageView)
                    .load(profileUrl)
                    .centerCrop()
                    .into(dataBinding.profileImageView);

            int likeCount = data.getCount().getLike_count();
            dataBinding.likeCountView.setText(String.valueOf(likeCount));

            int commentCount = data.getCount().getVideo_comment_count();
            dataBinding.commentCountView.setText(String.valueOf(commentCount));

            String username = data.getUser_info().getUsername();
            dataBinding.usernameView.setText(username);

            String description = data.getDescription();
            dataBinding.descriptionView.setText(description);
        }

        protected void initializePlayer() {

            if (exoPlayer == null) {
                setUpExoPlayer();
            }
            exoPlayer.setMediaSource(mediaSource);
            exoPlayer.prepare();
            exoPlayer.play();
            dataBinding.videoView.hideController();
        }

        private void setUpExoPlayer() {
            Context context = dataBinding.getRoot().getContext();

            exoPlayer = new SimpleExoPlayer.Builder(context)
                    .setMediaSourceFactory(new ProgressiveMediaSource.Factory(cacheDataSourceFactory))
                    .build();
            dataBinding.videoView.setPlayer(exoPlayer);
            exoPlayer.addMediaSource(mediaSource);
            exoPlayer.prepare();
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        }

        protected void stopPlayer() {
            exoPlayer.stop();
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
