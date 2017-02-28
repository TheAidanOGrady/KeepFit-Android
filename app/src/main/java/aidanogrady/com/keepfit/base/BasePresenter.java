package aidanogrady.com.keepfit.base;

/**
 * The base presenter.
 *
 * @author Aidan O'Grady
 * @since 0.1
 */
public interface BasePresenter<T> {
    /**
     * Sets the presenter to the given presenter.
     *
     * @param presenter the presenter to be set
     */
    void setPresenter(T presenter);
}
