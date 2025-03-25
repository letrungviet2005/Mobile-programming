@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.gkltdd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gkltdd.ui.theme.GKLTDDTheme
import com.example.gkltdd.R

data class Product(
    val id: Int,
    val imageRes: Int,
    val name: String,
    val description: String,
    val rating: Float,
    val price: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GKLTDDTheme {
            MainScreen()
            }
        }
    }
}

// Màn hình chính với AppBar và BottomBar cố định
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            TopAppBar(
                title = {
                    Text(
                        when (currentRoute) {
                            "product_list" -> "New Arrivals"
                            "product_detail/{productId}" -> "Product Details"
                            else -> "Shop"
                        },
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    if (currentRoute?.startsWith("product_detail") == true) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A5ACD))
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color(0xFF6A5ACD)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart", tint = Color.White)
                    Icon(Icons.Filled.Person, contentDescription = "Profile", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        NavHost(navController, startDestination = "product_list", Modifier.padding(paddingValues)) {
            composable("product_list") { ProductListScreen(navController, sampleProducts) }
            composable("product_detail/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                val product = sampleProducts.find { it.id == productId }
                product?.let { ProductDetailScreen(navController, it) }
            }
        }
    }
}

// Danh sách sản phẩm
@Composable
fun ProductListScreen(navController: NavHostController, products: List<Product>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(products) { product ->
            ProductItem(product, onClick = {
                navController.navigate("product_detail/${product.id}")
            })
        }
    }
}

// Mỗi sản phẩm trong danh sách
@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(115.dp, 160.dp)
            .clickable { onClick() }

    ) {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxHeight()
                    .width(120.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = product.name,
                    modifier = Modifier
                        .height(25.dp)
                        .fillMaxWidth(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = product.description,
                    modifier = Modifier
                        .requiredHeight(50.dp)
                        .fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    textAlign = TextAlign.Left,
                    softWrap = true
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "starRating",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Yellow
                    )


                    Text(
                        text = product.rating.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Left
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    onClick = {}
                ) {
                    Text(
                        text = "$${product.price}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(90.dp))
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add product to cart",
                        tint = Color.Black
                    )

                }
            }
        }
    }
}

// Màn hình chi tiết sản phẩm
@Composable
fun ProductDetailScreen(navController: NavHostController, product: Product) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxHeight()
    ) {
        Text(product.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Row(Modifier.padding(bottom = 10.dp)) {
            Text("⭐ ${product.rating} (2 Reviews)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        LazyColumn(
            Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item() {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = "Product image",
                    modifier = Modifier
                        .size(300.dp)
                )
            }

            item() {
                Text(product.description, fontSize = 14.sp)
            }

            item() { Divider() }

            item() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Reviews", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    IconButton(onClick = {
                        navController.navigate("manageReview/")
                    }) {
                        Row() {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "Add Review",
                                tint = Color.Blue
                            )
                        }
                    }
                }
                ReviewItem("Hoàng Thắng", "23/02/2025", 4f,"Tôi thích nó!", "Đây là một trong những sản phẩm thật hiếm có.")
                ReviewItem("Petrolimex", "24/02/2025", 5f, "Chất lượng tuyệt vời!", "Thật tuyệt vời với mức giá như vậy cho một sản phẩm như này.")
            }


        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("$${product.price}",fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Button(
                    onClick = { /* Thêm vào giỏ hàng */ },
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text("ADD TO CART")
                }
            }
        }
    }
}

// Đánh giá
@Composable
fun ReviewItem(name: String, date: String, stars: Float, title: String, review: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row() {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(25.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = name,
                        Modifier.padding(start = 5.dp, end = 5.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = date,
                    Modifier.padding(start = 5.dp, end = 5.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            Text("⭐".repeat(stars.toInt()))
            Text(title, fontWeight = FontWeight.Bold)
            Text(review)
        }
    }
}


val sampleProducts = listOf(
    Product(1,android.R.drawable.ic_menu_gallery, "Bàn cổ điển Hàn Quốc", "Chiếc bàn này được chế tác tại Hàn Quốc, với khung gỗ nâu sẫm và các đường nét chạm khắc tinh xảo. Mặt bàn vuông vắn, chân bàn tiện tròn, cùng các ngăn kéo trang trí tinh tế.", 4.5f, "12.99"),
    Product(2, android.R.drawable.ic_menu_gallery, "Ghế gỗ cổ điển Trung Quốc", "Ghế gỗ cổ điển Trung Quốc có xuất xứ từ Trung Quốc, triều Minh (1368–1644). Ghế làm từ gỗ hồng mộc quý hiếm, thiết kế tinh tế với lưng cong nhẹ, chân ghế chạm khắc hoa văn truyền thống. Màu gỗ nâu sẫm, bóng mượt, thể hiện sự thanh nhã và sang trọng.", 4.0f, "7.95"),
    Product(3, android.R.drawable.ic_menu_gallery, "Rương gỗ", "Rương cổ điển hình hộp chữ nhật, làm từ gỗ và khung sắt, màu nâu và đen. Có tay cầm da, khóa kim loại, in chữ First Class Istanbul, mang phong cách du lịch hạng nhất đầu thế kỷ 20.", 4.5f, "45.50"),
    Product(4, android.R.drawable.ic_menu_gallery, "Ấm trà Việt Nam", "Ấm trà cổ Việt Nam với nền men trắng, họa tiết hoa quả tươi sáng. Nắp kim loại vàng đồng, tay cầm thanh mảnh, phong cách cổ điển. Phù hợp làm hộp đựng trà, bánh hoặc trang trí, mang vẻ đẹp truyền thống và giá trị nghệ thuật tinh tế.", 4.0f, "57.95"),
)
