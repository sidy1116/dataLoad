/*     */ package com.oracle.qa.dataload.web.rest.security;

/*     */ public class Base64
/*     */ {
/*     */   static byte[] encodeData;
/*  37 */   static String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
/*     */   
/*     */   static
/*     */   {
/*  41 */     encodeData = new byte[64];
/*  42 */     for (int i = 0; i < 64; i++) {
/*  43 */       byte c = (byte)charSet.charAt(i);
/*  44 */       encodeData[i] = c;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(String s)
/*     */   {
/*  58 */     return encode(s.getBytes());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(byte[] src)
/*     */   {
/*  69 */     return encode(src, 0, src.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(byte[] src, int start, int length)
/*     */   {
/*  82 */     byte[] dst = new byte[(length + 2) / 3 * 4 + length / 72];
/*  83 */     int x = 0;
/*  84 */     int dstIndex = 0;
/*  85 */     int state = 0;
/*  86 */     int old = 0;
/*  87 */     int len = 0;
/*  88 */     int max = length + start;
/*  89 */     for (int srcIndex = start; srcIndex < max; srcIndex++) {
/*  90 */       x = src[srcIndex];
/*  91 */       state++; switch (state) {
/*     */       case 1: 
/*  93 */         dst[(dstIndex++)] = encodeData[(x >> 2 & 0x3F)];
/*  94 */         break;
/*     */       case 2: 
/*  96 */         dst[(dstIndex++)] = encodeData[(old << 4 & 0x30 | x >> 4 & 0xF)];
/*     */         
/*  98 */         break;
/*     */       case 3: 
/* 100 */         dst[(dstIndex++)] = encodeData[(old << 2 & 0x3C | x >> 6 & 0x3)];
/*     */         
/* 102 */         dst[(dstIndex++)] = encodeData[(x & 0x3F)];
/* 103 */         state = 0;
/*     */       }
/*     */       
/* 106 */       old = x;
/* 107 */       len++; if (len >= 72) {
/* 108 */         dst[(dstIndex++)] = 10;
/* 109 */         len = 0;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 117 */     switch (state) {
/* 118 */     case 1:  dst[(dstIndex++)] = encodeData[(old << 4 & 0x30)];
/* 119 */       dst[(dstIndex++)] = 61;
/* 120 */       dst[(dstIndex++)] = 61;
/* 121 */       break;
/* 122 */     case 2:  dst[(dstIndex++)] = encodeData[(old << 2 & 0x3C)];
/* 123 */       dst[(dstIndex++)] = 61;
/*     */     }
/*     */     
/* 126 */     return new String(dst);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] decode(String s)
/*     */   {
/* 139 */     int end = 0;
/* 140 */     if (s.endsWith("=")) {
/* 141 */       end++;
/*     */     }
/* 143 */     if (s.endsWith("==")) {
/* 144 */       end++;
/*     */     }
/* 146 */     int len = (s.length() + 3) / 4 * 3 - end;
/* 147 */     byte[] result = new byte[len];
/* 148 */     int dst = 0;
/*     */     try {
/* 150 */       for (int src = 0; src < s.length(); src++) {
/* 151 */         int code = charSet.indexOf(s.charAt(src));
/* 152 */         if (code == -1) {
/*     */           break;
/*     */         }
/* 155 */         switch (src % 4) {
/*     */         case 0: 
/* 157 */           result[dst] = ((byte)(code << 2));
/* 158 */           break;
/*     */         case 1: 
/* 160 */           int tmp134_131 = (dst++); byte[] tmp134_128 = result;tmp134_128[tmp134_131] = ((byte)(tmp134_128[tmp134_131] | (byte)(code >> 4 & 0x3)));
/* 161 */           result[dst] = ((byte)(code << 4));
/* 162 */           break;
/*     */         case 2: 
/* 164 */           int tmp164_161 = (dst++); byte[] tmp164_158 = result;tmp164_158[tmp164_161] = ((byte)(tmp164_158[tmp164_161] | (byte)(code >> 2 & 0xF)));
/* 165 */           result[dst] = ((byte)(code << 6));
/* 166 */           break;
/*     */         case 3: 
/* 168 */           int tmp196_193 = (dst++); byte[] tmp196_190 = result;tmp196_190[tmp196_193] = ((byte)(tmp196_190[tmp196_193] | (byte)(code & 0x3F)));
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
/* 173 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 

/*     */ }
