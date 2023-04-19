/* supplier*/

insert into mydb.supplier
values
('1', 'supplier1'),
('2', 'supplier2'),
('3', 'supplier3')

/* good */

insert into mydb.good
values 
('1', 'Philips monitor 17”')

/* sale */

insert into mydb.sale
values 
('1', '5.00', 'BILLTOTAL', '1000', '1'),	/*S1 has 8pcs in stock at 120€ each, and offers 5% discount for purchases of minimum 1000€. Min. days to ship order is 5*/
('2', '3.00', 'QUANTITY', '5', '2'),		/*S2 3% discount if you order >5pcs and 5% discount if you order >10pcs*/
('3', '5.00', 'QUANTITY', '10', '2'),		/* " */
('4', '5.00', 'BILLTOTAL', '1000', '3'),	/*S3 5% for orders over 1000€.*/
('5', '2.00', 'TIME', 'SEPTEMBER', '3')		/*	It also offers an additional discount of 2% for orders placed in september*/

/* supply */

insert into mydb.supply
values 
('1', '1', '1', '8', '120.00','5'), /*S 1 has 8pcs in stock at 120€ each, and... days to ship order is 5*/
('2', '1', '2', '15', '128.00','7'),/*S 2 has 15pcs in stock at 128€ each, and.. days to ship order is 7*/
('3', '1', '3', '15', '129.00','4')	/*S 3 has 23pcs in stock at 129€ each, and.. days to ship order is 4*/
