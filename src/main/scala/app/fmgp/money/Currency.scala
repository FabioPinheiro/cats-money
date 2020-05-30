package app.fmgp.money

import cats.kernel.Eq

object Currency {

  // def createCompanion[A](func: A => A): Companion[A] =
  //   new Companion[A] {
  //     override type C = A
  //     override def apply(): A = func(Currency.GBP.asInstanceOf[A]) //FIXME
  //   }

  // given companion as Companion [Currency.USD.type] {
  //   type C = Currency.USD.type
  //   def apply() = Currency.USD
  // }
}


/** Currency represents one currency of the ISO_4217 currencies list
 * @see [[https://en.wikipedia.org/wiki/ISO_4217]]
 */
enum Currency {
  /** AED have the numeric code 784*/ case AED
  /** AFN have the numeric code 971*/ case AFN
  /** ALL have the numeric code 008*/ case ALL
  /** AMD have the numeric code 051*/ case AMD
  /** ANG have the numeric code 532*/ case ANG
  /** AOA have the numeric code 973*/ case AOA
  /** ARS have the numeric code 032*/ case ARS
  /** AUD have the numeric code 036*/ case AUD
  /** AWG have the numeric code 533*/ case AWG
  /** AZN have the numeric code 944*/ case AZN
  /** BAM have the numeric code 977*/ case BAM
  /** BBD have the numeric code 052*/ case BBD
  /** BDT have the numeric code 050*/ case BDT
  /** BGN have the numeric code 975*/ case BGN
  /** BHD have the numeric code 048*/ case BHD
  /** BIF have the numeric code 108*/ case BIF
  /** BMD have the numeric code 060*/ case BMD
  /** BND have the numeric code 096*/ case BND
  /** BOB have the numeric code 068*/ case BOB
  /** BOV have the numeric code 984*/ case BOV
  /** BRL have the numeric code 986*/ case BRL
  /** BSD have the numeric code 044*/ case BSD
  /** BTN have the numeric code 064*/ case BTN
  /** BWP have the numeric code 072*/ case BWP
  /** BYN have the numeric code 933*/ case BYN
  /** BZD have the numeric code 084*/ case BZD
  /** CAD have the numeric code 124*/ case CAD
  /** CDF have the numeric code 976*/ case CDF
  /** CHE have the numeric code 947*/ case CHE
  /** CHF have the numeric code 756*/ case CHF
  /** CHW have the numeric code 948*/ case CHW
  /** CLF have the numeric code 990*/ case CLF
  /** CLP have the numeric code 152*/ case CLP
  /** CNY have the numeric code 156*/ case CNY
  /** COP have the numeric code 170*/ case COP
  /** COU have the numeric code 970*/ case COU
  /** CRC have the numeric code 188*/ case CRC
  /** CUC have the numeric code 931*/ case CUC
  /** CUP have the numeric code 192*/ case CUP
  /** CVE have the numeric code 132*/ case CVE
  /** CZK have the numeric code 203*/ case CZK
  /** DJF have the numeric code 262*/ case DJF
  /** DKK have the numeric code 208*/ case DKK
  /** DOP have the numeric code 214*/ case DOP
  /** DZD have the numeric code 012*/ case DZD
  /** EGP have the numeric code 818*/ case EGP
  /** ERN have the numeric code 232*/ case ERN
  /** ETB have the numeric code 230*/ case ETB
  /** EUR have the numeric code 978*/ case EUR
  /** FJD have the numeric code 242*/ case FJD
  /** FKP have the numeric code 238*/ case FKP
  /** GBP have the numeric code 826*/ case GBP
  /** GEL have the numeric code 981*/ case GEL
  /** GHS have the numeric code 936*/ case GHS
  /** GIP have the numeric code 292*/ case GIP
  /** GMD have the numeric code 270*/ case GMD
  /** GNF have the numeric code 324*/ case GNF
  /** GTQ have the numeric code 320*/ case GTQ
  /** GYD have the numeric code 328*/ case GYD
  /** HKD have the numeric code 344*/ case HKD
  /** HNL have the numeric code 340*/ case HNL
  /** HRK have the numeric code 191*/ case HRK
  /** HTG have the numeric code 332*/ case HTG
  /** HUF have the numeric code 348*/ case HUF
  /** IDR have the numeric code 360*/ case IDR
  /** ILS have the numeric code 376*/ case ILS
  /** INR have the numeric code 356*/ case INR
  /** IQD have the numeric code 368*/ case IQD
  /** IRR have the numeric code 364*/ case IRR
  /** ISK have the numeric code 352*/ case ISK
  /** JMD have the numeric code 388*/ case JMD
  /** JOD have the numeric code 400*/ case JOD
  /** JPY have the numeric code 392*/ case JPY
  /** KES have the numeric code 404*/ case KES
  /** KGS have the numeric code 417*/ case KGS
  /** KHR have the numeric code 116*/ case KHR
  /** KMF have the numeric code 174*/ case KMF
  /** KPW have the numeric code 408*/ case KPW
  /** KRW have the numeric code 410*/ case KRW
  /** KWD have the numeric code 414*/ case KWD
  /** KYD have the numeric code 136*/ case KYD
  /** KZT have the numeric code 398*/ case KZT
  /** LAK have the numeric code 418*/ case LAK
  /** LBP have the numeric code 422*/ case LBP
  /** LKR have the numeric code 144*/ case LKR
  /** LRD have the numeric code 430*/ case LRD
  /** LSL have the numeric code 426*/ case LSL
  /** LYD have the numeric code 434*/ case LYD
  /** MAD have the numeric code 504*/ case MAD
  /** MDL have the numeric code 498*/ case MDL
  /** MGA have the numeric code 969*/ case MGA
  /** MKD have the numeric code 807*/ case MKD
  /** MMK have the numeric code 104*/ case MMK
  /** MNT have the numeric code 496*/ case MNT
  /** MOP have the numeric code 446*/ case MOP
  /** MRU have the numeric code 929*/ case MRU
  /** MUR have the numeric code 480*/ case MUR
  /** MVR have the numeric code 462*/ case MVR
  /** MWK have the numeric code 454*/ case MWK
  /** MXN have the numeric code 484*/ case MXN
  /** MXV have the numeric code 979*/ case MXV
  /** MYR have the numeric code 458*/ case MYR
  /** MZN have the numeric code 943*/ case MZN
  /** NAD have the numeric code 516*/ case NAD
  /** NGN have the numeric code 566*/ case NGN
  /** NIO have the numeric code 558*/ case NIO
  /** NOK have the numeric code 578*/ case NOK
  /** NPR have the numeric code 524*/ case NPR
  /** NZD have the numeric code 554*/ case NZD
  /** OMR have the numeric code 512*/ case OMR
  /** PAB have the numeric code 590*/ case PAB
  /** PEN have the numeric code 604*/ case PEN
  /** PGK have the numeric code 598*/ case PGK
  /** PHP have the numeric code 608*/ case PHP
  /** PKR have the numeric code 586*/ case PKR
  /** PLN have the numeric code 985*/ case PLN
  /** PYG have the numeric code 600*/ case PYG
  /** QAR have the numeric code 634*/ case QAR
  /** RON have the numeric code 946*/ case RON
  /** RSD have the numeric code 941*/ case RSD
  /** RUB have the numeric code 643*/ case RUB
  /** RWF have the numeric code 646*/ case RWF
  /** SAR have the numeric code 682*/ case SAR
  /** SBD have the numeric code 090*/ case SBD
  /** SCR have the numeric code 690*/ case SCR
  /** SDG have the numeric code 938*/ case SDG
  /** SEK have the numeric code 752*/ case SEK
  /** SGD have the numeric code 702*/ case SGD
  /** SHP have the numeric code 654*/ case SHP
  /** SLL have the numeric code 694*/ case SLL
  /** SOS have the numeric code 706*/ case SOS
  /** SRD have the numeric code 968*/ case SRD
  /** SSP have the numeric code 728*/ case SSP
  /** STN have the numeric code 930*/ case STN
  /** SVC have the numeric code 222*/ case SVC
  /** SYP have the numeric code 760*/ case SYP
  /** SZL have the numeric code 748*/ case SZL
  /** THB have the numeric code 764*/ case THB
  /** TJS have the numeric code 972*/ case TJS
  /** TMT have the numeric code 934*/ case TMT
  /** TND have the numeric code 788*/ case TND
  /** TOP have the numeric code 776*/ case TOP
  /** TRY have the numeric code 949*/ case TRY
  /** TTD have the numeric code 780*/ case TTD
  /** TWD have the numeric code 901*/ case TWD
  /** TZS have the numeric code 834*/ case TZS
  /** UAH have the numeric code 980*/ case UAH
  /** UGX have the numeric code 800*/ case UGX
  /** USD have the numeric code 840*/ case USD
  /** USN have the numeric code 997*/ case USN
  /** UYI have the numeric code 940*/ case UYI
  /** UYU have the numeric code 858*/ case UYU
  /** UYW have the numeric code 927*/ case UYW
  /** UZS have the numeric code 860*/ case UZS
  /** VES have the numeric code 928*/ case VES
  /** VND have the numeric code 704*/ case VND
  /** VUV have the numeric code 548*/ case VUV
  /** WST have the numeric code 882*/ case WST
  /** XAF have the numeric code 950*/ case XAF
  /** XAG have the numeric code 961*/ case XAG
  /** XAU have the numeric code 959*/ case XAU
  /** XBA have the numeric code 955*/ case XBA
  /** XBB have the numeric code 956*/ case XBB
  /** XBC have the numeric code 957*/ case XBC
  /** XBD have the numeric code 958*/ case XBD
  /** XCD have the numeric code 951*/ case XCD
  /** XDR have the numeric code 960*/ case XDR
  /** XOF have the numeric code 952*/ case XOF
  /** XPD have the numeric code 964*/ case XPD
  /** XPF have the numeric code 953*/ case XPF
  /** XPT have the numeric code 962*/ case XPT
  /** XSU have the numeric code 994*/ case XSU
  /** XTS have the numeric code 963*/ case XTS
  /** XUA have the numeric code 965*/ case XUA
  /** XXX have the numeric code 999*/ case XXX
  /** YER have the numeric code 886*/ case YER
  /** ZAR have the numeric code 710*/ case ZAR
  /** ZMW have the numeric code 967*/ case ZMW
  /** ZWL have the numeric code 932*/ case ZWL
}
