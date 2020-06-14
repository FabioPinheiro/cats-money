module.exports = {
  title: 'cats-money',
  tagline: 'Scala functional library to deal amounts of money',  //'A type-safe money holder'
  url: 'https://cats-money.fmgp.app/',  //FIXME
  baseUrl: '/',
  favicon: 'img/favicon.ico',
  organizationName: 'FabioPinheiro', // Usually your GitHub org/user name.
  projectName: 'cats-money', // Usually your repo name.
  url: 'https://FabioPinheiro.github.io',
  baseUrl: '/cats-money/',
  themeConfig: {
    navbar: {
      title: 'Cats-Money',
      logo: {
        alt: 'cats-money-logo',
        src: 'img/logo.ico',
      },
      links: [
        {
          to: 'docs/',
          activeBasePath: 'docs',
          label: 'Docs',
          position: 'right',
        },
        {
          to: 'blog',
          label: 'Blog',
          position: 'right'
        },
        {
          href: 'https://github.com/FabioPinheiro/cats-money',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Docs',
          items: [
            {
              label: 'Get started',
              to: 'docs/',
            },
            // {
            //   label: 'Second Doc',
            //   to: 'docs/doc2/',
            // },
          ],
        },
        {
          title: 'Community',
          items: [
            {
              label: 'Chat on gitter - TODO',
              href: 'https://gitter.im/FabioPinheiro/cats-money',
            },
            // {
            //   label: 'Stack Overflow',
            //   href: 'https://stackoverflow.com/questions/tagged/cats-money',
            // },
            {
              label: 'Twitter',
              href: 'https://twitter.com/fabiomgpinheiro',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'Blog',
              to: 'blog',
            },
            {
              label: 'GitHub',
              href: 'https://github.com/FabioPinheiro/cats-money',
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} cats-money developers.`,
    },
  },
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          // It is recommended to set document id as docs home page (`docs/` path).
          homePageId: 'getting-started',
          sidebarPath: require.resolve('./sidebars.js'),
          // Please change this to your repo.
          editUrl: 'https://github.com/FabioPinheiro/cats-money/edit/master/website/',
        },
        blog: {
          showReadingTime: true,
          // Please change this to your repo.
          editUrl: 'https://github.com/FabioPinheiro/cats-money/edit/master/website/blog/',
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      },
    ],
  ],
};
