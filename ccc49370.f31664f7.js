(window.webpackJsonp=window.webpackJsonp||[]).push([[18],{163:function(e,a,t){"use strict";t.r(a);var n=t(0),l=t.n(n),i=t(168),r=t(184),m=t(165);var c=function(e){var a=e.nextItem,t=e.prevItem;return l.a.createElement("nav",{className:"pagination-nav","aria-label":"Blog post page navigation"},l.a.createElement("div",{className:"pagination-nav__item"},t&&l.a.createElement(m.a,{className:"pagination-nav__link",to:t.permalink},l.a.createElement("div",{className:"pagination-nav__sublabel"},"Previous Post"),l.a.createElement("div",{className:"pagination-nav__label"},"\xab ",t.title))),l.a.createElement("div",{className:"pagination-nav__item pagination-nav__item--next"},a&&l.a.createElement(m.a,{className:"pagination-nav__link",to:a.permalink},l.a.createElement("div",{className:"pagination-nav__sublabel"},"Next Post"),l.a.createElement("div",{className:"pagination-nav__label"},a.title," \xbb"))))};a.default=function(e){var a=e.content,t=a.frontMatter,n=a.metadata,m=n.title,s=n.description,o=n.nextItem,v=n.prevItem,p=n.editUrl;return l.a.createElement(i.a,{title:m,description:s},a&&l.a.createElement("div",{className:"container margin-vert--lg"},l.a.createElement("div",{className:"row"},l.a.createElement("div",{className:"col col--8 col--offset-2"},l.a.createElement(r.a,{frontMatter:t,metadata:n,isBlogPostPage:!0},l.a.createElement(a,null)),l.a.createElement("div",null,p&&l.a.createElement("a",{href:p,target:"_blank",rel:"noreferrer noopener"},l.a.createElement("svg",{fill:"currentColor",height:"1.2em",width:"1.2em",preserveAspectRatio:"xMidYMid meet",viewBox:"0 0 40 40",style:{marginRight:"0.3em",verticalAlign:"sub"}},l.a.createElement("g",null,l.a.createElement("path",{d:"m34.5 11.7l-3 3.1-6.3-6.3 3.1-3q0.5-0.5 1.2-0.5t1.1 0.5l3.9 3.9q0.5 0.4 0.5 1.1t-0.5 1.2z m-29.5 17.1l18.4-18.5 6.3 6.3-18.4 18.4h-6.3v-6.2z"}))),"Edit this page")),(o||v)&&l.a.createElement("div",{className:"margin-vert--xl"},l.a.createElement(c,{nextItem:o,prevItem:v}))))))}}}]);