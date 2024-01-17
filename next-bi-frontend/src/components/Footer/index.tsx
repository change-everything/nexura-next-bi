import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import '@umijs/max';
import React from 'react';
const Footer: React.FC = () => {
  const defaultMessage = 'peiYP 出品';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'next BI',
          title: 'next BI',
          href: 'https://github.com/change-everything/nexura-next-bi',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/change-everything/nexura-next-bi',
          blankTarget: true,
        },
        {
          key: 'next BI',
          title: 'next BI',
          href: 'https://github.com/change-everything/nexura-next-bi',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
