// Initialize charts when the page loads
document.addEventListener('DOMContentLoaded', function() {
    initializeDoughnutChart();
    initializeLiveAlertChart();
    initializeBarChart();
    initializeHighlightChart();
});

// Doughnut Chart for Clear City Location
function initializeDoughnutChart() {
    const ctx = document.getElementById('doughnutChart').getContext('2d');
    
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            datasets: [{
                data: [30, 25, 20, 15, 10],
                backgroundColor: [
                    '#f59e0b',
                    '#ef4444',
                    '#3b82f6',
                    '#10b981',
                    '#8b5cf6'
                ],
                borderWidth: 0,
                cutout: '70%'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    enabled: false
                }
            }
        }
    });
}

// Live Alert Line Chart
function initializeLiveAlertChart() {
    const ctx = document.getElementById('liveAlertChart').getContext('2d');
    
    const data = [20, 35, 25, 45, 30, 50, 40, 55, 35, 60, 45, 40];
    
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: Array.from({length: 12}, (_, i) => i + 1),
            datasets: [{
                data: data,
                borderColor: '#3b82f6',
                backgroundColor: 'rgba(59, 130, 246, 0.1)',
                borderWidth: 2,
                fill: true,
                tension: 0.4,
                pointRadius: 0,
                pointHoverRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    display: false
                },
                y: {
                    display: false
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    enabled: false
                }
            },
            elements: {
                point: {
                    radius: 0
                }
            }
        }
    });
}

// Bar Chart for Insughten
function initializeBarChart() {
    const ctx = document.getElementById('barChart').getContext('2d');
    
    const data = [60, 30, 40, 20, 35, 50, 45, 25, 40, 70];
    
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: Array.from({length: 10}, (_, i) => i + 1),
            datasets: [{
                data: data,
                backgroundColor: data.map((value, index) => {
                    if (index === 9) return '#3b82f6'; // Highlight the last bar
                    return '#e2e8f0';
                }),
                borderRadius: 4,
                borderSkipped: false
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    display: false
                },
                y: {
                    display: false
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    enabled: false
                }
            }
        }
    });
}

// Highlight Line Chart
function initializeHighlightChart() {
    const ctx = document.getElementById('highlightChart').getContext('2d');
    
    const data = [30, 45, 35, 55, 40, 50, 35, 45, 30, 40, 35, 30];
    
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: Array.from({length: 12}, (_, i) => i + 1),
            datasets: [{
                data: data,
                borderColor: '#10b981',
                backgroundColor: 'rgba(16, 185, 129, 0.1)',
                borderWidth: 2,
                fill: true,
                tension: 0.4,
                pointRadius: 0,
                pointHoverRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    display: false
                },
                y: {
                    display: false
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    enabled: false
                }
            },
            elements: {
                point: {
                    radius: 0
                }
            }
        }
    });
}

// Add click handlers for navigation items
document.addEventListener('DOMContentLoaded', function() {
    const navItems = document.querySelectorAll('.nav-item');
    
    navItems.forEach(item => {
        item.addEventListener('click', function() {
            // Remove active class from all items
            navItems.forEach(nav => nav.classList.remove('active'));
            // Add active class to clicked item
            this.classList.add('active');
        });
    });
});

// Add search functionality
document.addEventListener('DOMContentLoaded', function() {
    const searchInputs = document.querySelectorAll('input[type="text"]');
    
    searchInputs.forEach(input => {
        input.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                console.log('Search for:', this.value);
                // Add your search logic here
            }
        });
    });
});

// Add city dropdown functionality
document.addEventListener('DOMContentLoaded', function() {
    const cityDropdown = document.querySelector('.city-dropdown');
    
    if (cityDropdown) {
        cityDropdown.addEventListener('click', function() {
            console.log('City dropdown clicked');
            // Add dropdown menu logic here
        });
    }
});

// Add action button functionality
document.addEventListener('DOMContentLoaded', function() {
    const actionBtn = document.querySelector('.action-btn');
    
    if (actionBtn) {
        actionBtn.addEventListener('click', function() {
            console.log('Action button clicked');
            // Add action logic here
        });
    }
});
