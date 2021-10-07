package com.android.example.shortvideos.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.example.shortvideos.R;
import com.android.example.shortvideos.databinding.VideoRecyclerItemBinding;
import com.android.example.shortvideos.models.MediaData;
import com.android.example.shortvideos.util.CacheUtil;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class VideoListAdapter extends
        ListAdapter<MediaData, VideoListAdapter.ViewHolder> {


    public VideoListAdapter(@NonNull DiffUtil.ItemCallback<MediaData> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent, this);
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

    public ArrayList<String> getNextUrlToCache(int currentPos) {
        ArrayList<String> arrayList = new ArrayList<>();
        String url1 = getCurrentList().get(currentPos + 1).getVideo();
        String url2 = getCurrentList().get(currentPos + 2).getVideo();
        String url3 = getCurrentList().get(currentPos + 3).getVideo();

        arrayList.add(url1);
        arrayList.add(url2);
        arrayList.add(url3);
        Timber.d("Array List %s", arrayList);
        return arrayList;
    }

    @NonNull
    @Override
    public List<MediaData> getCurrentList() {
        return super.getCurrentList();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final VideoRecyclerItemBinding dataBinding;
        private SimpleExoPlayer exoPlayer;
        private final VideoListAdapter adapter;
        MediaItem item;
        private CacheDataSource.Factory cacheDataSourceFactory;
        private MediaSourceFactory mediaSource;

        private ViewHolder(@NonNull VideoRecyclerItemBinding dataBinding, VideoListAdapter adapter) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
            this.adapter = adapter;
        }

        public static ViewHolder from(@NonNull ViewGroup parent, VideoListAdapter adapter) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            VideoRecyclerItemBinding dataBinding = VideoRecyclerItemBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(dataBinding, adapter);
        }

        protected void onBind(MediaData data) {

            cacheData(data);
            setUpUserInfo(data);
        }

        private void cacheData(MediaData mediaData) {
            Context context = dataBinding.getRoot().getContext();

            Uri videoUrl = Uri.parse(mediaData.getVideo());

            DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(
                    context,
                    Util.getUserAgent(context, context.getString(R.string.app_name))
            );

            CacheDataSource.Factory factory = new CacheDataSource
                    .Factory()
                    .setCache(CacheUtil.simpleCache)
                    .setUpstreamDataSourceFactory(defaultDataSourceFactory);

            CacheUtil.cacheVideos(adapter.getNextUrlToCache(getAbsoluteAdapterPosition()), context);

            MediaSource mediaSource =
                    new ProgressiveMediaSource
                            .Factory(factory)
                            .createMediaSource(MediaItem.fromUri(videoUrl));

            ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
            concatenatingMediaSource.addMediaSource(mediaSource);

            exoPlayer = new SimpleExoPlayer.Builder(context)
                    .build();

            exoPlayer.addMediaSource(concatenatingMediaSource);
            exoPlayer.prepare();

            dataBinding.videoView.setPlayer(exoPlayer);
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        }

        private void setUpUserInfo(MediaData data) {
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

            exoPlayer.play();
            dataBinding.videoView.hideController();
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
