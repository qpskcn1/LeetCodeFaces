package com.google.ar.techin515.justice.augmentedfaces;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.*;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Random;

public class LeetcodeFace extends AugmentedFaceNode {
    private Node cardNode;
    private TextView textView;
    private Handler mHandler;
    private Runnable mRunnable;
    private final String[] problems;
    private final Context context;
    private ViewRenderable uiRenderable;

    public LeetcodeFace(@Nullable AugmentedFace augmentedFace, Context context) {
        super(augmentedFace);
        this.context = context;
        this.problems = new String[]{
                "Array",
                "Dynamic Programming",
                "Tree",
                "DFS",
                "BFS",
                "Hash Table",
                "Binary Search",
                "Greedy",
                "Linked List",
                "Divide and Conquer",
                "Binary Search Tree",
                "Sliding Window",
                "Heap"
        };
        Log.d("LeetcodeFace", "Created");
    }

    @Override
    public void onActivate() {
        super.onActivate();
        this.cardNode = new Node();
        this.cardNode.setParent(this);
        this.mHandler = new Handler();
        ViewRenderable.builder()
                .setView(this.context, R.layout.card_layout)
                .build()
                .thenAccept(
                        ViewRenderable -> {
                            uiRenderable = ViewRenderable;
                            uiRenderable.setShadowCaster(false);
                            uiRenderable.setShadowReceiver(false);
                            this.cardNode.setRenderable(uiRenderable);
                            LeetcodeFace.this.textView = uiRenderable.getView().findViewById(R.id.title);
                        }).exceptionally(throwable -> {
            throw new AssertionError("Could not create ui element", throwable);
        });
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        AugmentedFace face = this.getAugmentedFace();
        if (face != null) {
            AugmentedFace f = face;
            Pose rightForehead = face.getRegionPose((AugmentedFace.RegionType.FOREHEAD_RIGHT));
            Pose leftForehead = face.getRegionPose((AugmentedFace.RegionType.FOREHEAD_LEFT));
            Pose center = face.getCenterPose();
            this.cardNode.setWorldPosition(new Vector3((leftForehead.tx() + rightForehead.tx()) / 2,
                    (leftForehead.ty() + rightForehead.ty()) / 2 + 0.05f , center.tz()));
        }
    }

    public final String[] getProblems() {
        return this.problems;
    }

    public final Context getContext() {
        return this.context;
    }

    public final void animate() {
//        Log.d("animate", "Here");
        Random Dice = new Random();
        int index = Dice.nextInt(problems.length);
        int rounds = Dice.nextInt(2) + 2;
        final int[] currentIndex = {0};
        final int[] currentRound = {0};

        mRunnable = () -> {
            TextView prompt = LeetcodeFace.this.textView;
            String[] problems = LeetcodeFace.this.getProblems();
            prompt.setText(problems[currentIndex[0]]);
            currentIndex[0]++;
            if (currentIndex[0] == problems.length) {
                currentIndex[0] = 0;
                currentRound[0]++;
            }
            if (currentRound[0] == rounds) {
                prompt.setText(problems[index]);
            } else {
                mHandler.postDelayed(mRunnable, 100);
            }
        };

        mHandler.postDelayed(mRunnable, 100);
    }

    public final void refresh() {
        Log.d("refresh", "Here");
        TextView prompt = this.textView;
        if (prompt != null) {
            prompt.setText("Start Your LeetCode Grinding!");
        }
    }
}
