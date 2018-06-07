package com.csy.fight.preview;

/**
 * Created by csy on 2018/5/18 14:40
 * @author csy
 */
public class PreviewPresenter implements IPreviewContract.IPresenter {

    private IPreviewContract.IView mPreviewView;

    public PreviewPresenter() {
    }

    @Override
    public Object getData() {
        return null;
    }

    public void setPreviewView(IPreviewContract.IView mPreviewView) {
        this.mPreviewView = mPreviewView;
        mPreviewView.setPresenter(this);
    }
}
