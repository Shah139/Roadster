// Initialize page when DOM loads
document.addEventListener('DOMContentLoaded', function() {
    initializeSearch();
    initializeFilters();
    initializeCards();
});

// Search functionality
function initializeSearch() {
    const searchInput = document.getElementById('searchInput');
    
    searchInput.addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        filterCards(searchTerm);
    });
}

// Filter functionality
function initializeFilters() {
    const filterDropdowns = document.querySelectorAll('.filter-dropdown');
    
    filterDropdowns.forEach(dropdown => {
        dropdown.addEventListener('change', function() {
            applyFilters();
        });
    });
}

// Filter cards based on search and filters
function filterCards(searchTerm = '') {
    const cards = document.querySelectorAll('.police-box-card');
    
    cards.forEach(card => {
        const title = card.querySelector('h4').textContent.toLowerCase();
        const location = card.querySelector('.location').textContent.toLowerCase();
        const district = card.querySelector('.district').textContent.toLowerCase();
        
        const matchesSearch = title.includes(searchTerm) || 
                             location.includes(searchTerm) || 
                             district.includes(searchTerm);
        
        if (matchesSearch) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

// Apply all filters
function applyFilters() {
    const districtFilter = document.querySelector('.filter-dropdown').value;
    const statusFilter = document.querySelectorAll('.filter-dropdown')[1].value;
    const cards = document.querySelectorAll('.police-box-card');
    
    cards.forEach(card => {
        const district = card.querySelector('.district').textContent.toLowerCase();
        const status = card.classList.contains('active') ? 'active' : 
                      card.classList.contains('maintenance') ? 'maintenance' : 'offline';
        
        const matchesDistrict = districtFilter === 'all' || district.includes(districtFilter);
        const matchesStatus = statusFilter === 'all' || status === statusFilter;
        
        if (matchesDistrict && matchesStatus) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
    
    updateStatistics();
}

// Initialize card interactions
function initializeCards() {
    const cards = document.querySelectorAll('.police-box-card');
    
    cards.forEach(card => {
        // View Details button
        const viewBtn = card.querySelector('.btn-primary');
        viewBtn.addEventListener('click', function() {
            const boxId = card.querySelector('.box-id').textContent;
            const title = card.querySelector('h4').textContent;
            showPoliceBoxDetails(boxId, title);
        });
        
        // Edit/Repair button
        const actionBtn = card.querySelector('.btn-secondary');
        actionBtn.addEventListener('click', function() {
            const boxId = card.querySelector('.box-id').textContent;
            const isOffline = card.classList.contains('offline');
            
            if (isOffline) {
                initiateRepair(boxId);
            } else {
                editPoliceBox(boxId);
            }
        });
    });
}

// Show police box details
function showPoliceBoxDetails(boxId, title) {
    alert(`Viewing details for ${boxId}: ${title}\n\nThis would open a detailed view with:\n- Real-time status\n- Activity logs\n- Maintenance history\n- Officer assignments\n- Performance metrics`);
}

// Edit police box
function editPoliceBox(boxId) {
    alert(`Editing ${boxId}\n\nThis would open an edit form with:\n- Location settings\n- Officer assignments\n- Maintenance schedule\n- Equipment configuration`);
}

// Initiate repair
function initiateRepair(boxId) {
    const confirmRepair = confirm(`Initiate repair request for ${boxId}?`);
    if (confirmRepair) {
        alert(`Repair request submitted for ${boxId}\n\nA technician will be dispatched within 2 hours.`);
        // In a real application, this would update the database and UI
    }
}

// Update statistics based on visible cards
function updateStatistics() {
    const visibleCards = Array.from(document.querySelectorAll('.police-box-card')).filter(card => 
        card.style.display !== 'none'
    );
    
    const activeCount = visibleCards.filter(card => card.classList.contains('active')).length;
    const maintenanceCount = visibleCards.filter(card => card.classList.contains('maintenance')).length;
    const offlineCount = visibleCards.filter(card => card.classList.contains('offline')).length;
    const totalCount = visibleCards.length;
    
    // Update the statistics cards
    const statCards = document.querySelectorAll('.stat-card');
    statCards[0].querySelector('h3').textContent = activeCount;
    statCards[1].querySelector('h3').textContent = maintenanceCount;
    statCards[2].querySelector('h3').textContent = offlineCount;
    statCards[3].querySelector('h3').textContent = totalCount;
}

// Add new police box
document.addEventListener('DOMContentLoaded', function() {
    const addBtn = document.querySelector('.action-btn');
    
    if (addBtn) {
        addBtn.addEventListener('click', function() {
            alert('Add New Police Box\n\nThis would open a form to:\n- Select location on map\n- Assign district\n- Set initial configuration\n- Assign responsible officer\n- Schedule installation');
        });
    }
});

// Real-time status updates (simulated)
function simulateRealTimeUpdates() {
    setInterval(() => {
        const cards = document.querySelectorAll('.police-box-card');
        
        cards.forEach(card => {
            const lastCheckElement = card.querySelector('.stat-item .value');
            if (lastCheckElement && lastCheckElement.textContent.includes('ago')) {
                // Simulate time progression
                const currentText = lastCheckElement.textContent;
                if (currentText.includes('min ago')) {
                    const minutes = parseInt(currentText);
                    if (minutes < 59) {
                        lastCheckElement.textContent = `${minutes + 1} min ago`;
                    } else {
                        lastCheckElement.textContent = '1 hour ago';
                    }
                } else if (currentText.includes('hour ago')) {
                    const hours = parseInt(currentText);
                    lastCheckElement.textContent = `${hours + 1} hours ago`;
                }
            }
        });
    }, 60000); // Update every minute
}

// Start real-time updates
document.addEventListener('DOMContentLoaded', function() {
    // Uncomment to enable real-time updates
    // simulateRealTimeUpdates();
});

// Navigation helpers
function navigateToDashboard() {
    window.location.href = '../dashboard/index.html';
}

function navigateToMap() {
    window.location.href = '../maps/index.html';
}
