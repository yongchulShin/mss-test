import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터
api.interceptors.request.use(
  (config) => {
    console.log('API Request:', config.method?.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터
api.interceptors.response.use(
  (response) => {
    console.log('API Response:', response.status, response.data);
    return response;
  },
  (error) => {
    console.error('API Error:', error.response?.status, error.response?.data);
    return Promise.reject(error);
  }
);

export const productAPI = {
  // 카테고리별 최저가 조회
  getLowestByCategory: () => api.get('/product/summary/lowest'),
  
  // 브랜드별 전체 카테고리 최저가 조회
  getLowestBrandForAllCategories: () => api.get('/product/summary/brand-lowest'),
  
  // 카테고리별 최저/최고가 조회
  getMinMaxByCategory: (categoryName) => api.get(`/product/summary/category/${categoryName}`),
};

export const brandAPI = {
  // 브랜드 생성
  createBrand: (brandData) => api.post('/brand', brandData),
  
  // 브랜드 수정
  updateBrand: (brandId, brandData) => api.put(`/brand/${brandId}`, brandData),
  
  // 브랜드 삭제
  deleteBrand: (brandId) => api.delete(`/brand/${brandId}`),
};

export default api; 