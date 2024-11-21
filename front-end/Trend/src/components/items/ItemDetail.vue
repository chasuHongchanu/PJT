<template>
    <div class="item-detail">
      <img :src="photoUrl" alt="Item Image" class="item-image" />
      <div class="item-info">
        <h3 class="item-name">{{ name }}</h3>
        <p class="item-price">{{ formatPrice(price) }}</p>
        <p class="item-address">{{ address }}</p>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "ItemDetail",
    props: {
      photoUrl: {
        type: String,
        required: true,
      },
      name: {
        type: String,
        required: true,
      },
      price: {
        type: Number,
        required: true,
      },
      address: {
        type: String,
        required: true,
      },
    },
    methods: {
      formatPrice(price) {
        return `${price.toLocaleString()}원`;
      },
    },
  };
  </script>
  
  <style scoped>
  .item-detail {
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 10px;
    text-align: center;
    background: #fff;
  }
  .item-image {
    width: 100%;
    height: auto;
    border-radius: 4px;
  }
  .item-info {
    margin-top: 10px;
  }
  .item-name {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 5px;
  }
  .item-price {
    color: green;
    font-size: 14px;
    margin-bottom: 5px;
  }
  .item-address {
    font-size: 12px;
    color: gray;
  }
  </style>
  