import React from 'react';
import classnames from 'classnames';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import useBaseUrl from '@docusaurus/useBaseUrl';
import styles from './styles.module.css';

const features = [
  {
    title: <>Type-safe</>,
    //imageUrl: 'img/undraw_docusaurus_tree.svg',
    description: (
      <>
        The cats-money was designed to allow work with different currencies in a type-safe manner.
        So it&apos;s not possible to mix currencies in an unsafe way.
      </>
    ),
  },
  {
    title: <>Easy to Use</>,
    //imageUrl: 'img/undraw_docusaurus_mountain.svg',
    description: (
      <>
        This library was designed clean, simple and easy to use.
        Implements basic ops on money, you can convert different currencies, etc.
        10 minutes learning curve! Go ahead and move your <code>docs</code> to getStarted.
      </>
    ),
  },
  {
    title: <>Generic & traceable</>,
    //imageUrl: 'img/undraw_docusaurus_react.svg',
    description: (
      <>
        We intend to be genetic. You shound define the set of currencies and all instances (by mixing traits in a object) to adapter your specific use case.
        One important thing is traceability (WIP). It's being design in top of the other properties.
      </>
    ),
  },
];

function Feature({ imageUrl, title, description }) {
  const imgUrl = useBaseUrl(imageUrl);
  return (
    <div className={classnames('col col--4', styles.feature)}>
      {imgUrl && (
        <div className="text--center">
          <img className={styles.featureImage} src={imgUrl} alt={title} />
        </div>
      )}
      <h3>{title}</h3>
      <p>{description}</p>
    </div>
  );
}

function Home() {
  const context = useDocusaurusContext();
  const { siteConfig = {} } = context;
  return (
    <Layout
      title={`${siteConfig.title}`}
      description="Documentation website of cats-maney">
      <header className={classnames('hero hero--primary', styles.heroBanner)}>
        <div className="container">
          <h1 className="hero__title">{siteConfig.title}</h1>
          <p className="hero__subtitle">{siteConfig.tagline}</p>
          <div className={styles.buttons}>
            <Link
              className={classnames(
                'button button--outline button--secondary button--lg',
                styles.getStarted,
              )}
              to={useBaseUrl('docs/')}>
              Get Started
            </Link>
          </div>
        </div>
      </header>
      <main>
        {features && features.length > 0 && (
          <section className={styles.features}>
            <div className="container">
              <div className="row">
                {features.map((props, idx) => (
                  <Feature key={idx} {...props} />
                ))}
              </div>
            </div>
          </section>
        )}
      </main>
    </Layout>
  );
}

export default Home;
