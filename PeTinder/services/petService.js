import { useState, useEffect, useCallback } from 'react';
import mockData from '../data/db.json';
// import api from '../api'; // Uncomment when connecting to real backend

/**
 * Pet Service - Data abstraction layer
 * 
 * Currently uses mock data from db.json
 * TODO: Replace mock functions with API calls when backend is ready
 * 
 * To switch to real API:
 * 1. Uncomment the api import above
 * 2. Replace the mock implementations with API calls
 * 3. Update the useOngInfo hook to get data from auth context
 */

// ============================================
// MOCK DATA FUNCTIONS (replace with API calls)
// ============================================

const fetchPetsFromMock = () => {
  return new Promise((resolve) => {
    // Simulate network delay
    setTimeout(() => {
      resolve(mockData.pets);
    }, 300);
  });
};

const fetchOngFromMock = () => {
  return new Promise((resolve) => {
    // Simulate network delay
    setTimeout(() => {
      resolve(mockData.ong);
    }, 200);
  });
};

// ============================================
// API FUNCTIONS (uncomment when backend ready)
// ============================================

// const fetchPetsFromAPI = async () => {
//   const response = await api.get('/pets');
//   return response.data;
// };

// const fetchOngFromAPI = async () => {
//   const response = await api.get('/ong/me'); // Get logged in ONG info
//   return response.data;
// };

// ============================================
// HOOKS
// ============================================

/**
 * Hook to fetch and manage pets data
 * @returns {Object} { pets, loading, error, refetch, adoptedCount, notAdoptedCount }
 */
export const usePets = () => {
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchPets = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Switch this line when connecting to real API:
      const data = await fetchPetsFromMock();
      // const data = await fetchPetsFromAPI();
      
      setPets(data);
    } catch (err) {
      setError(err.message || 'Erro ao carregar pets');
      console.error('Error fetching pets:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchPets();
  }, [fetchPets]);

  // Computed values
  const adoptedCount = pets.filter(pet => pet.adopted).length;
  const notAdoptedCount = pets.filter(pet => !pet.adopted).length;
  const totalCount = pets.length;
  
  // Sort pets by likes (descending) for the bar chart
  const petsSortedByLikes = [...pets].sort((a, b) => b.likes - a.likes);

  return {
    pets,
    petsSortedByLikes,
    loading,
    error,
    refetch: fetchPets,
    adoptedCount,
    notAdoptedCount,
    totalCount,
  };
};

/**
 * Hook to fetch and manage ONG info
 * 
 * TODO: When implementing auth, this should:
 * 1. Get ONG info from AuthContext
 * 2. Or fetch from /ong/me endpoint using auth token
 * 
 * @returns {Object} { ong, loading, error }
 */
export const useOngInfo = () => {
  const [ong, setOng] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchOng = async () => {
      try {
        setLoading(true);
        setError(null);
        
        // Switch this line when connecting to real API/Auth:
        const data = await fetchOngFromMock();
        // const data = await fetchOngFromAPI();
        // Or: const data = authContext.user; // From auth context
        
        setOng(data);
      } catch (err) {
        setError(err.message || 'Erro ao carregar informações da ONG');
        console.error('Error fetching ONG info:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchOng();
  }, []);

  return { ong, loading, error };
};

/**
 * Helper function to get adoption statistics
 * @param {Array} pets - Array of pet objects
 * @returns {Object} { adopted, notAdopted, total, adoptionRate }
 */
export const getAdoptionStats = (pets) => {
  const adopted = pets.filter(pet => pet.adopted).length;
  const notAdopted = pets.filter(pet => !pet.adopted).length;
  const total = pets.length;
  const adoptionRate = total > 0 ? (adopted / total) * 100 : 0;

  return {
    adopted,
    notAdopted,
    total,
    adoptionRate: Math.round(adoptionRate),
  };
};

export default {
  usePets,
  useOngInfo,
  getAdoptionStats,
};
