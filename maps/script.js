// Initialize map when page loads
document.addEventListener('DOMContentLoaded', function() {
    initializeMap();
    initializeToggles();
    initializeModal();
    initializeSearch();
});

let map;
let markers = {};
let layers = {};

// Initialize the Leaflet map
function initializeMap() {
    // Initialize map centered on a city location
    map = L.map('map').setView([40.7128, -74.0060], 13);

    // Add tile layer (OpenStreetMap)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    // Create layer groups
    layers.crimeZones = L.layerGroup().addTo(map);
    layers.cctv = L.layerGroup().addTo(map);
    layers.policeStations = L.layerGroup();
    layers.hospitals = L.layerGroup();
    layers.congestedRoads = L.layerGroup();

    // Add sample data
    addSampleData();
    
    // Add click handler for road details
    map.on('click', function(e) {
        showRoadDetails(e.latlng);
    });
}

// Add sample markers and data to the map
function addSampleData() {
    // Crime zones (red areas)
    const crimeZone1 = L.circle([40.7589, -73.9851], {
        color: '#e74c3c',
        fillColor: '#e74c3c',
        fillOpacity: 0.3,
        radius: 500
    }).addTo(layers.crimeZones);
    
    const crimeZone2 = L.circle([40.7282, -74.0776], {
        color: '#e74c3c',
        fillColor: '#e74c3c',
        fillOpacity: 0.3,
        radius: 300
    }).addTo(layers.crimeZones);

    // CCTV cameras
    const cctvIcon = L.divIcon({
        html: '<i class="fas fa-video" style="color: #3498db; font-size: 16px;"></i>',
        iconSize: [20, 20],
        className: 'custom-marker'
    });

    L.marker([40.7505, -73.9934], {icon: cctvIcon})
        .bindPopup('CCTV Camera - Times Square')
        .addTo(layers.cctv);
    
    L.marker([40.7589, -73.9851], {icon: cctvIcon})
        .bindPopup('CCTV Camera - Central Park')
        .addTo(layers.cctv);

    // Police stations
    const policeIcon = L.divIcon({
        html: '<i class="fas fa-shield-alt" style="color: #2c3e50; font-size: 16px;"></i>',
        iconSize: [20, 20],
        className: 'custom-marker'
    });

    L.marker([40.7614, -73.9776], {icon: policeIcon})
        .bindPopup('Police Station - Midtown North')
        .addTo(layers.policeStations);

    // Hospitals
    const hospitalIcon = L.divIcon({
        html: '<i class="fas fa-hospital" style="color: #e74c3c; font-size: 16px;"></i>',
        iconSize: [20, 20],
        className: 'custom-marker'
    });

    L.marker([40.7796, -73.9632], {icon: hospitalIcon})
        .bindPopup('Mount Sinai Hospital')
        .addTo(layers.hospitals);

    // Congested roads
    const congestedRoad1 = L.polyline([
        [40.7505, -73.9934],
        [40.7589, -73.9851],
        [40.7614, -73.9776]
    ], {
        color: '#f39c12',
        weight: 6,
        opacity: 0.8
    }).addTo(layers.congestedRoads);

    // Add a special marker for the road details demo
    const roadMarker = L.marker([40.7128, -74.0060], {
        icon: L.divIcon({
            html: '<div style="background: #e74c3c; width: 20px; height: 20px; border-radius: 50%; border: 3px solid white; box-shadow: 0 2px 4px rgba(0,0,0,0.3);"></div>',
            iconSize: [20, 20],
            className: 'road-issue-marker'
        })
    }).addTo(map);

    roadMarker.on('click', function() {
        showRoadDetailsModal();
    });
}

// Initialize toggle switches
function initializeToggles() {
    const toggles = document.querySelectorAll('.toggle-switch');
    
    toggles.forEach(toggle => {
        toggle.addEventListener('click', function() {
            const layerName = this.getAttribute('data-layer');
            
            if (this.classList.contains('active')) {
                this.classList.remove('active');
                hideLayer(layerName);
            } else {
                this.classList.add('active');
                showLayer(layerName);
            }
        });
    });
}

// Show/hide map layers
function showLayer(layerName) {
    switch(layerName) {
        case 'crime-zones':
            map.addLayer(layers.crimeZones);
            break;
        case 'cctv':
            map.addLayer(layers.cctv);
            break;
        case 'congested-roads':
            map.addLayer(layers.congestedRoads);
            break;
        case 'listen':
            // Add listen layer logic here
            break;
    }
}

function hideLayer(layerName) {
    switch(layerName) {
        case 'crime-zones':
            map.removeLayer(layers.crimeZones);
            break;
        case 'cctv':
            map.removeLayer(layers.cctv);
            break;
        case 'congested-roads':
            map.removeLayer(layers.congestedRoads);
            break;
        case 'listen':
            // Remove listen layer logic here
            break;
    }
}

// Initialize modal functionality
function initializeModal() {
    const modal = document.getElementById('roadDetailsModal');
    const closeBtn = document.getElementById('closeModal');
    
    closeBtn.addEventListener('click', function() {
        hideRoadDetailsModal();
    });
    
    // Close modal when clicking outside
    document.addEventListener('click', function(e) {
        if (modal.classList.contains('active') && !modal.contains(e.target) && !e.target.closest('.road-issue-marker')) {
            hideRoadDetailsModal();
        }
    });
}

// Show road details modal
function showRoadDetailsModal() {
    const modal = document.getElementById('roadDetailsModal');
    modal.classList.add('active');
}

// Hide road details modal
function hideRoadDetailsModal() {
    const modal = document.getElementById('roadDetailsModal');
    modal.classList.remove('active');
}

// Show road details popup
function showRoadDetails(latlng) {
    const popup = L.popup()
        .setLatLng(latlng)
        .setContent(`
            <div style="text-align: center; padding: 10px;">
                <h4>Road Information</h4>
                <p>Lat: ${latlng.lat.toFixed(4)}</p>
                <p>Lng: ${latlng.lng.toFixed(4)}</p>
                <button onclick="showRoadDetailsModal()" style="
                    background: #3498db; 
                    color: white; 
                    border: none; 
                    padding: 8px 16px; 
                    border-radius: 4px; 
                    cursor: pointer;
                    margin-top: 8px;
                ">
                    More Details
                </button>
            </div>
        `)
        .openOn(map);
}

// Initialize search functionality
function initializeSearch() {
    const searchInput = document.getElementById('searchInput');
    
    searchInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            performSearch(this.value);
        }
    });
    
    searchInput.addEventListener('input', function() {
        if (this.value.length > 2) {
            performLiveSearch(this.value);
        }
    });
}

// Perform search
function performSearch(query) {
    console.log('Searching for:', query);
    
    // Sample search functionality - in real app, this would connect to a geocoding service
    if (query.toLowerCase().includes('hospital')) {
        map.addLayer(layers.hospitals);
        map.setView([40.7796, -73.9632], 15);
    } else if (query.toLowerCase().includes('police')) {
        map.addLayer(layers.policeStations);
        map.setView([40.7614, -73.9776], 15);
    } else if (query.toLowerCase().includes('crime')) {
        map.addLayer(layers.crimeZones);
        map.setView([40.7589, -73.9851], 14);
    }
}

// Perform live search (autocomplete)
function performLiveSearch(query) {
    // Implement autocomplete functionality here
    console.log('Live searching for:', query);
}

// AI Assistant functionality
document.addEventListener('DOMContentLoaded', function() {
    const aiAssistant = document.querySelector('.ai-assistant');
    
    if (aiAssistant) {
        aiAssistant.addEventListener('click', function() {
            alert('AI Assistant: How can I help you navigate the city today?');
            // In a real app, this would open an AI chat interface
        });
    }
});

// Modal action buttons
document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('roadDetailsModal');
    
    if (modal) {
        // Go to Details button
        const detailsBtn = modal.querySelector('.btn-secondary');
        if (detailsBtn) {
            detailsBtn.addEventListener('click', function() {
                console.log('Navigate to detailed road information');
                // Implement navigation to detailed view
            });
        }
        
        // Report Issue button
        const reportBtn = modal.querySelector('.btn-primary');
        if (reportBtn) {
            reportBtn.addEventListener('click', function() {
                console.log('Report road issue');
                alert('Thank you for reporting this issue. Our team will investigate shortly.');
                hideRoadDetailsModal();
            });
        }
    }
});

// Notification handlers
document.addEventListener('DOMContentLoaded', function() {
    const notifications = document.querySelectorAll('.notification-icon');
    
    notifications.forEach(notification => {
        notification.addEventListener('click', function() {
            const iconType = this.querySelector('i').classList.contains('fa-bell') ? 'notifications' : 'messages';
            console.log(`Opening ${iconType}`);
            // Implement notification/message panel
        });
    });
});

// Custom map controls
function addCustomMapControls() {
    // Add custom zoom controls or other map-specific controls
    const customControl = L.control({position: 'topleft'});
    
    customControl.onAdd = function(map) {
        const div = L.DomUtil.create('div', 'custom-map-control');
        div.innerHTML = `
            <button onclick="resetMapView()" style="
                background: white;
                border: none;
                padding: 8px 12px;
                border-radius: 4px;
                cursor: pointer;
                box-shadow: 0 2px 4px rgba(0,0,0,0.2);
            ">
                <i class="fas fa-home"></i> Reset View
            </button>
        `;
        return div;
    };
    
    customControl.addTo(map);
}

// Reset map to default view
function resetMapView() {
    map.setView([40.7128, -74.0060], 13);
}

// Initialize custom controls after map is ready
setTimeout(() => {
    if (map) {
        addCustomMapControls();
    }
}, 1000);
