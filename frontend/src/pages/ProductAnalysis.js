import React, { useState } from 'react';
import styled from 'styled-components';
import { productAPI } from '../services/api';

const Container = styled.div`
  padding: 20px;
`;

const Title = styled.h1`
  color: #333;
  margin-bottom: 30px;
  text-align: center;
`;

const SearchContainer = styled.div`
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  max-width: 600px;
  margin: 0 auto 30px;
`;

const Form = styled.form`
  display: flex;
  gap: 15px;
  align-items: end;
`;

const FormGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
`;

const Label = styled.label`
  font-weight: 500;
  color: #333;
`;

const Select = styled.select`
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  
  &:focus {
    outline: none;
    border-color: #667eea;
    box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
  }
`;

const Button = styled.button`
  padding: 12px 24px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
  
  &:hover {
    background: #5a6fd8;
  }
  
  &:disabled {
    background: #ccc;
    cursor: not-allowed;
  }
`;

const ResultContainer = styled.div`
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  max-width: 800px;
  margin: 0 auto;
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

const PriceSection = styled.div`
  margin: 20px 0;
  padding: 20px;
  border-radius: 4px;
  
  ${props => props.type === 'min' && `
    background: #e8f5e8;
    border: 1px solid #c3e6cb;
  `}
  
  ${props => props.type === 'max' && `
    background: #ffeaea;
    border: 1px solid #f5c6cb;
  `}
`;

const PriceTitle = styled.h3`
  margin: 0 0 15px 0;
  color: #333;
`;

const PriceList = styled.div`
  display: grid;
  gap: 10px;
`;

const PriceItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background: white;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
`;

const BrandName = styled.span`
  font-weight: 500;
  color: #333;
`;

const Price = styled.span`
  font-weight: bold;
  color: #667eea;
`;

const categories = [
  '상의', '아우터', '바지', '스니커즈', '가방', '모자', '양말', '액세서리'
];

function ProductAnalysis() {
  const [selectedCategory, setSelectedCategory] = useState('');
  const [analysisResult, setAnalysisResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!selectedCategory) {
      setError('카테고리를 선택해주세요.');
      return;
    }

    setLoading(true);
    setError(null);
    setAnalysisResult(null);

    try {
      const response = await productAPI.getMinMaxByCategory(selectedCategory);
      setAnalysisResult(response.data);
    } catch (err) {
      const errorMessage = err.response?.data?.message || '분석 중 오류가 발생했습니다.';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container>
      <Title>상품 분석</Title>
      
      <SearchContainer>
        <Form onSubmit={handleSubmit}>
          <FormGroup>
            <Label>카테고리 선택</Label>
            <Select
              value={selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
              required
            >
              <option value="">카테고리를 선택하세요</option>
              {categories.map(category => (
                <option key={category} value={category}>
                  {category}
                </option>
              ))}
            </Select>
          </FormGroup>
          
          <Button type="submit" disabled={loading || !selectedCategory}>
            {loading ? '분석 중...' : '분석하기'}
          </Button>
        </Form>
      </SearchContainer>

      {error && <ErrorMessage>{error}</ErrorMessage>}

      {loading && (
        <ResultContainer>
          <LoadingSpinner>분석 중...</LoadingSpinner>
        </ResultContainer>
      )}

      {analysisResult && (
        <ResultContainer>
          <h2>{analysisResult.카테고리} 카테고리 분석 결과</h2>
          
          <PriceSection type="min">
            <PriceTitle>최저가 브랜드</PriceTitle>
            <PriceList>
              {analysisResult.최저가?.map((item, index) => (
                <PriceItem key={index}>
                  <BrandName>{item.브랜드}</BrandName>
                  <Price>{item.가격}</Price>
                </PriceItem>
              ))}
            </PriceList>
          </PriceSection>

          <PriceSection type="max">
            <PriceTitle>최고가 브랜드</PriceTitle>
            <PriceList>
              {analysisResult.최고가?.map((item, index) => (
                <PriceItem key={index}>
                  <BrandName>{item.브랜드}</BrandName>
                  <Price>{item.가격}</Price>
                </PriceItem>
              ))}
            </PriceList>
          </PriceSection>
        </ResultContainer>
      )}
    </Container>
  );
}

export default ProductAnalysis; 