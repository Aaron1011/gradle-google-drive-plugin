package ru.kinca.gradle.googledrive

import org.gradle.api.internal.provider.AbstractProvider
import org.gradle.api.provider.PropertyState
import org.gradle.api.provider.Provider

/**
 * Property state that is instantiated with a default value. If no value was set
 * explicitly, the default one is used. The default value may be null.
 *
 * @author Valentin Naumov
 */
class PropertyStateWithDefaultValue<T> implements PropertyState<T>
{
    private static final NULL_PROVIDER = { null }

    private final Closure<T> defaultValueProvider
    private Provider<? extends T> provider;

    /**
     * Creates the property state with default value of {@code null}.
     */
    PropertyStateWithDefaultValue()
    {
        this(NULL_PROVIDER)
    }

    PropertyStateWithDefaultValue(
        T defaultValue)
    {
        this({ defaultValue })
    }

    PropertyStateWithDefaultValue(
        Closure<T> defaultValueProvider)
    {
        this.defaultValueProvider = defaultValueProvider
        this.provider = null
    }

    /**
     * If a value is present in this provider, returns the default value.
     *
     * @return value
     * @throws IllegalStateException if there is no value present
     */
    @Override
    T get()
    {
        if (provider != null) {
            T value = provider.getOrNull();
            if (value != null) {
                return value;
            }
        }
        return defaultValueProvider.call()
    }

    /**
     * Returns {@code true} if there is a value present, otherwise the default
     * value.
     *
     * @return {@code true} if there is a value present, otherwise the default
     * value.
     */
    @Override
    T getOrNull()
    {
        get()
    }

    @Override
    boolean isPresent() {
        return get() != null
    }

    @Override
    void set(T value) {
        this.provider = new AbstractProvider<T>() {

            @Override
            T getOrNull() {
                return value
            }
        }
    }

    @Override
    void set(Provider<? extends T> provider) {
        this.provider = provider
    }
}
