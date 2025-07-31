const map = new maplibregl.Map({
    container: 'map',
    style: 'https://api.maptiler.com/maps/streets-v2/style.json?key=uSZHAA0ZFcoLru0ECNJq', // Sample style
    center: [90.4125, 23.8103], // Example: Dhaka
    zoom: 13
});

// Example marker
const marker = new maplibregl.Marker({ color: 'red' })
    .setLngLat([90.4125, 23.8103])
    .addTo(map);
