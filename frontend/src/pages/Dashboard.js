import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { productAPI } from '../services/api';

const DashboardContainer = styled.div`
  padding: 20px;
`;

const Title = styled.h1`
  color: #333;
  margin-bottom: 30px;
  text-align: center;
`;

const StatsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
`;

const StatCard = styled.div`
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  border-left: 4px solid #667eea;
`;

const StatTitle = styled.h3`
  color: #666;
  margin-bottom: 10px;
  font-size: 0.9rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
`;

const StatValue = styled.div`
  font-size: 2rem;
  font-weight: bold;
  color: #333;
`;

const LoadingSpinner = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  font-size: 1.2rem;
  color: #666;
`;

const ErrorMessage = styled.div`
  background: #fee;
  color: #c33;
  padding: 15px;
  border-radius: 4px;
  margin: 20px 0;
  border-left: 4px solid #c33;
`;

const CategoryList = styled.div`
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
`;

const CategoryItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  
  &:last-child {
    border-bottom: none;
  }
`;

const CategoryName = styled.span`
  font-weight: 500;
  color: #333;
`;

const CategoryPrice = styled.span`
  color: #667eea;
  font-weight: bold;
`;

function Dashboard() {
  const [lowestByCategory, setLowestByCategory] = useState(null);
  const [lowestBrand, setLowestBrand] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        setError(null);
        
        const [categoryResponse, brandResponse] = await Promise.all([
          productAPI.getLowestByCategory(),
          productAPI.getLowestBrandForAllCategories()
        ]);
        
        setLowestByCategory(categoryResponse.data);
        setLowestBrand(brandResponse.data);
      } catch (err) {
        setError('데이터를 불러오는 중 오류가 발생했습니다.');
        console.error('Dashboard fetch error:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return (
      <DashboardContainer>
        <Title>Musinsa 상품 분석 대시보드</Title>
        <LoadingSpinner>데이터를 불러오는 중...</LoadingSpinner>
      </DashboardContainer>
    );
  }

  if (error) {
    return (
      <DashboardContainer>
        <Title>Musinsa 상품 분석 대시보드</Title>
        <ErrorMessage>{error}</ErrorMessage>
      </DashboardContainer>
    );
  }

  return (
    <DashboardContainer>
      <Title>Musinsa 상품 분석 대시보드</Title>
      
      <StatsGrid>
        <StatCard>
          <StatTitle>카테고리별 최저가 총액</StatTitle>
          <StatValue>₩{lowestByCategory?.총액?.toLocaleString()}</StatValue>
        </StatCard>
        
        <StatCard>
          <StatTitle>최저가 브랜드</StatTitle>
          <StatValue>{lowestBrand?.최저가?.브랜드}</StatValue>
        </StatCard>
        
        <StatCard>
          <StatTitle>최저가 브랜드 총액</StatTitle>
          <StatValue>{lowestBrand?.최저가?.총액}</StatValue>
        </StatCard>
      </StatsGrid>

      <CategoryList>
        <h3>카테고리별 최저가</h3>
        {lowestByCategory?.카테고리?.map((item, index) => (
          <CategoryItem key={index}>
            <CategoryName>{item.카테고리}</CategoryName>
            <CategoryPrice>
              {item.브랜드} - ₩{item.가격?.toLocaleString()}
            </CategoryPrice>
          </CategoryItem>
        ))}
      </CategoryList>
    </DashboardContainer>
  );
}

export default Dashboard; 