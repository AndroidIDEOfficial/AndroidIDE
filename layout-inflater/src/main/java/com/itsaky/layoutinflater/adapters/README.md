# Attribute Adapters
Attribute adapters handle attributes for a specific view.

## Implementing an Adapter
### Basic
To implment an adapter, you'll have to write a class extending one of the
existing adapters.

Adapters extend other adapters in the same way as Android's view classes extend other views.
For example, ```android.widget.CompoundButton``` extends ```android.widget.Button```, in the same way
```com.itsaky.layoutinflater.adapters.android.widget.CompoundButtonAttrAdapter``` extends
```com.itsaky.layoutinflater.adapters.android.widget.ButtonAttrAdapter```. In this way, we don't have to
rewrite whole logic again to implement adapters for every other widget.

### Naming an adapter
If you look at the package name and the simple name of the adapter classes mentioned above,
you'll find that there is a specific way an adapter is named.

An adpater should be named in the following way :
```com.itsaky.layoutinflater.adapters.<package_name>.<simple_name>AttrAdapter```,
where, ```<package_name>``` is the package name of the widget you are implementing an adapter for and ```<simple_name>``` is the simple name of the widget.
The simple name is then suffixed with ```AttrAdapter```.

Here is an example :
Assume we have to implement an adapter for ```android.widget.Button```.
Here, ```<package_name>``` is ```android.widget``` and
```<simple_name>``` is ```Button```.

So, the adapter implentation class should be ```com.itsaky.layoutinflater.adapters.android.widget.ButtonAttrAdapter```.

### Why such naming?
We use reflection to automatically create an adapter when inflating a view.
For example, if ```android.widget.Button``` instance is being created, we could simply create an adapter instance using reflection by getting the package name and simple name of the widget.

See ```XMLLayoutInflater#onCreateAttributeAdapter(View)```.
