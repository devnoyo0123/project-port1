'use client';

import Script from 'next/script';

export default function PortOneScript() {
  return (
    <Script
      src="https://cdn.iamport.kr/v1/iamport.js"
      strategy="beforeInteractive"
      onReady={() => {
        console.log('PortOne script is ready to use');
        if (typeof window !== 'undefined' && window.IMP) {
          window.IMP.init('your_imp_key_here'); // 실제 키로 교체 필요
          console.log('PortOne SDK initialized');
        }
      }}
      onError={(e: Error) => {
        console.error('PortOne script failed to load', e);
      }}
    />
  );
} 