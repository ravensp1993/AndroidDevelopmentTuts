
package android.databinding;
import com.example.android.beatbox.BR;
class DataBinderMapper  {
    final static int TARGET_MIN_SDK = 19;
    public DataBinderMapper() {
    }
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View view, int layoutId) {
        switch(layoutId) {
                case com.example.android.beatbox.R.layout.list_item_sound:
                    return com.example.android.beatbox.databinding.ListItemSoundBinding.bind(view, bindingComponent);
                case com.example.android.beatbox.R.layout.beat_box_fragment:
                    return com.example.android.beatbox.databinding.BeatBoxFragmentBinding.bind(view, bindingComponent);
        }
        return null;
    }
    android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View[] views, int layoutId) {
        switch(layoutId) {
        }
        return null;
    }
    int getLayoutId(String tag) {
        if (tag == null) {
            return 0;
        }
        final int code = tag.hashCode();
        switch(code) {
            case -1732174288: {
                if(tag.equals("layout/list_item_sound_0")) {
                    return com.example.android.beatbox.R.layout.list_item_sound;
                }
                break;
            }
            case -1017755079: {
                if(tag.equals("layout/beat_box_fragment_0")) {
                    return com.example.android.beatbox.R.layout.beat_box_fragment;
                }
                break;
            }
        }
        return 0;
    }
    String convertBrIdToString(int id) {
        if (id < 0 || id >= InnerBrLookup.sKeys.length) {
            return null;
        }
        return InnerBrLookup.sKeys[id];
    }
    private static class InnerBrLookup {
        static String[] sKeys = new String[]{
            "_all"
            ,"mSound"
            ,"viewModel"};
    }
}