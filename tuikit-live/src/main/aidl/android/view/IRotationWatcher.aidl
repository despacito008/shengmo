//RotationWatcher.aidl

package android.view;

/**
* {@hide}
*/
interface IRotationWatcher {
    void onRotationChanged(int rotation);
}