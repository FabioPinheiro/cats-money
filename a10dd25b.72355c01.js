(window.webpackJsonp=window.webpackJsonp||[]).push([[15],{154:function(e,n,t){"use strict";t.r(n),t.d(n,"frontMatter",(function(){return r})),t.d(n,"metadata",(function(){return i})),t.d(n,"rightToc",(function(){return p})),t.d(n,"default",(function(){return s}));var a=t(2),o=t(9),c=(t(0),t(166)),r={id:"money",title:"Money[T]",sidebar_label:"Money[T]"},i={id:"money",isDocsHomePage:!1,title:"Money[T]",description:"Money[C] represents amount of money in a specific currency C.",source:"@site/docs/money.md",permalink:"/cats-money/docs/money",editUrl:"https://github.com/FabioPinheiro/cats-money/edit/master/website/docs/money.md",sidebar_label:"Money[T]",sidebar:"someSidebar",previous:{title:"Getting started",permalink:"/cats-money/docs/"},next:{title:"MoneyTree[T]",permalink:"/cats-money/docs/money-tree"}},p=[],m={rightToc:p};function s(e){var n=e.components,t=Object(o.a)(e,["components"]);return Object(c.b)("wrapper",Object(a.a)({},m,t,{components:n,mdxType:"MDXLayout"}),Object(c.b)("p",null,Object(c.b)("strong",{parentName:"p"},Object(c.b)("inlineCode",{parentName:"strong"},"Money[C]"))," represents amount of money in a specific currency ",Object(c.b)("strong",{parentName:"p"},Object(c.b)("inlineCode",{parentName:"strong"},"C")),".\nIt can be used as a stand-alone if you are only working with one type of money.\nIt can be used with ",Object(c.b)("a",Object(a.a)({parentName:"p"},{href:"/cats-money/docs/money-tree"}),Object(c.b)("inlineCode",{parentName:"a"},"MoneyTree[T]"))," to mix different currencies."),Object(c.b)("p",null,Object(c.b)("inlineCode",{parentName:"p"},"Money[C]")," used a ",Object(c.b)("inlineCode",{parentName:"p"},"BigDecimal")," for the amount so it may have more precision while doing calculations."),Object(c.b)("pre",null,Object(c.b)("code",Object(a.a)({parentName:"pre"},{className:"language-scala"}),"Money can be used in the following way:\n> Money(1, USD) + Money(5, USD)\napp.fmgp.money.Money[app.fmgp.money.Currency.USD.type] = Money(6,app.fmgp.money.Currency$USD)\n")),Object(c.b)("pre",null,Object(c.b)("code",Object(a.a)({parentName:"pre"},{className:"language-scala"}),"> Money(100, USD) / 5\napp.fmgp.money.Money[app.fmgp.money.Currency.USD.type] = Money(20,app.fmgp.money.Currency$USD)\n")),Object(c.b)("div",{className:"admonition admonition-caution alert alert--warning"},Object(c.b)("div",Object(a.a)({parentName:"div"},{className:"admonition-heading"}),Object(c.b)("h5",{parentName:"div"},Object(c.b)("span",Object(a.a)({parentName:"h5"},{className:"admonition-icon"}),Object(c.b)("svg",Object(a.a)({parentName:"span"},{xmlns:"http://www.w3.org/2000/svg",width:"16",height:"16",viewBox:"0 0 16 16"}),Object(c.b)("path",Object(a.a)({parentName:"svg"},{fillRule:"evenodd",d:"M8.893 1.5c-.183-.31-.52-.5-.887-.5s-.703.19-.886.5L.138 13.499a.98.98 0 0 0 0 1.001c.193.31.53.501.886.501h13.964c.367 0 .704-.19.877-.5a1.03 1.03 0 0 0 .01-1.002L8.893 1.5zm.133 11.497H6.987v-2.003h2.039v2.003zm0-3.004H6.987V5.987h2.039v4.006z"})))),"caution")),Object(c.b)("div",Object(a.a)({parentName:"div"},{className:"admonition-content"}),Object(c.b)("p",{parentName:"div"},"It's not possible to add different types of money!\nThe following code will intentional not compile:"),Object(c.b)("pre",{parentName:"div"},Object(c.b)("code",Object(a.a)({parentName:"pre"},{className:"language-scala"}),"> Money(50, USD) + Money(100, EUR)\nerror: Cannot prove that app.fmgp.money.Currency.EUR.type =:= app.fmgp.money.Currency.USD.type.\n")))),Object(c.b)("p",null,"You can find here the source code of ",Object(c.b)("a",Object(a.a)({parentName:"p"},{href:"https://github.com/FabioPinheiro/cats-money/blob/master/src/main/scala/app/fmgp/money/Money.scala",title:"Money[T] source code"}),"Money[T]")))}s.isMDXComponent=!0}}]);