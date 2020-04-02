# scultura 

Interactive music sheet based on works of Bogusław Schaeffer (`Symfonia - Muzyka Elektroniczna`) to be specific. Users can place various objects (defined in the sheet for Symfonia) and then play music generated from the sheet and change it on the fly.

Samples were created following instructions included in sheet music, but if you would like to use your own replace the ones included in resources/public/samples with your custom samples while keeping dir structure and naming intact.
## Production Build


To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```
